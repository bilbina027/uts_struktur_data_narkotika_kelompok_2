package com.pengadilan.app.service;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.util.DynamicArray;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Locale;

public final class CsvService {
    public record ImportResult(
            DynamicArray<Putusan> records,
            int validRows,
            int invalidRows,
            DynamicArray<String> errors) {
        public boolean acceptable() {
            return validRows == 50 && invalidRows == 0 && errors.isEmpty();
        }

        public String errorText() {
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < errors.size(); i++) {
                if (i > 0) text.append('\n');
                text.append(errors.get(i));
            }
            return text.toString();
        }
    }

    public ImportResult importCsv(File file) throws IOException {
        DynamicArray<Putusan> staging = new DynamicArray<>(50);
        DynamicArray<String> errors = new DynamicArray<>();
        int invalid = 0;
        int count = 0;
        try (Reader input = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            CsvReader reader = new CsvReader(input);
            String[] header = reader.readRecord();
            String[] expected = Putusan.headers();
            boolean validHeader = header != null && header.length == 16;
            if (validHeader) {
                for (int i = 0; i < 16; i++) {
                    if (!expected[i].equals(header[i].trim())) {
                        validHeader = false;
                        break;
                    }
                }
            }
            if (!validHeader) {
                errors.add("Header harus sesuai 16 atribut baku.");
                return new ImportResult(staging, 0, 1, errors);
            }
            while (true) {
                String[] fields;
                try {
                    fields = reader.readRecord();
                } catch (IOException exception) {
                    invalid++;
                    errors.add("Sintaks CSV setelah record " + count + ": " + exception.getMessage());
                    break;
                }
                if (fields == null) break;
                count++;
                try {
                    Putusan p = Putusan.fromFields(fields);
                    DataManager.ensureUnique(staging, p);
                    staging.add(p);
                } catch (RuntimeException exception) {
                    invalid++;
                    errors.add("Record " + count + ": " + exception.getMessage());
                }
            }
        }
        if (count != 50) {
            errors.add("Harus 50 record; ditemukan " + count + ".");
        }
        if (staging.size() != 50) {
            errors.add("Record valid: " + staging.size() + ", seharusnya 50.");
        }
        return new ImportResult(staging, staging.size(), invalid, errors);
    }

    public void exportCsv(File file, DynamicArray<Putusan> records) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writeRecord(writer, Putusan.headers());
            for (int i = 0; i < records.size(); i++) {
                writeRecord(writer, records.get(i).toFields());
            }
        }
    }

    private void writeRecord(Writer writer, String[] fields) throws IOException {
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) writer.write(',');
            writer.write(escapeCsv(fields[i]));
        }
        writer.write("\r\n");
    }

    public static String escapeCsv(String value) {
        if (value == null) return "";
        boolean quoted = value.contains(",") || value.contains("\"")
                || value.contains("\n") || value.contains("\r");
        String escaped = value.replace("\"", "\"\"");
        return quoted ? "\"" + escaped + "\"" : escaped;
    }

    public void exportReport(File file, DynamicArray<Putusan> records) throws IOException {
        DataManager.Ringkasan summary = DataManager.summarize(records);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write("LAPORAN PUTUSAN NARKOTIKA\n");
            writer.write("Waktu: " + LocalDateTime.now() + "\n");
            writer.write("Jumlah: " + summary.jumlah() + "\n");
            writer.write(String.format(Locale.ROOT, "Rata-rata vonis: %.2f bulan%n", summary.rataVonis()));
            writer.write("Total denda: Rp " + summary.totalDenda() + "\n\n");
            for (int i = 0; i < records.size(); i++) {
                Putusan p = records.get(i);
                writer.write(p.getCaseId() + " " + p.getNomorPerkara() + " | " + p.getPengadilan() + "\n");
            }
        }
    }

    public void exportTrace(File file, DynamicArray<String> trace) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write("TRACE INSERTION SORT\n");
            for (int i = 0; i < trace.size(); i++) {
                writer.write(trace.get(i));
                writer.newLine();
            }
        }
    }

    public static final class CsvReader {
        private final PushbackReader reader;
        private boolean first = true;

        public CsvReader(Reader reader) {
            this.reader = new PushbackReader(reader, 1);
        }

        public String[] readRecord() throws IOException {
            String[] fields = new String[16];
            int count = 0;
            StringBuilder value = new StringBuilder();
            boolean quoted = false;
            boolean closed = false;
            boolean start = true;
            boolean seen = false;
            while (true) {
                int raw = reader.read();
                if (first) {
                    first = false;
                    if (raw == 0xFEFF) raw = reader.read();
                }
                if (raw == -1) {
                    if (quoted) {
                        throw new IOException("Quote tidak berpasangan.");
                    }
                    if (!seen && count == 0 && value.isEmpty()) return null;
                    return finish(fields, count, value);
                }
                seen = true;
                char c = (char) raw;
                if (quoted) {
                    if (c == '"') {
                        int next = reader.read();
                        if (next == '"') {
                            value.append('"');
                        } else {
                            quoted = false;
                            closed = true;
                            if (next != -1) {
                                reader.unread(next);
                            }
                        }
                        continue;
                    } else {
                        value.append(c);
                        continue;
                    }
                }
                if (closed && c != ',' && c != '\r' && c != '\n') {
                    throw new IOException("Karakter setelah quote tidak valid.");
                }
                if (c == ',') {
                    if (count >= 16) throw new IOException("Kolom melebihi 16.");
                    fields[count++] = value.toString();
                    value.setLength(0);
                    closed = false;
                    start = true;
                    continue;
                }
                if (c == '\r' || c == '\n') {
                    if (c == '\r') {
                        int next = reader.read();
                        if (next != '\n' && next != -1) reader.unread(next);
                    }
                    return finish(fields, count, value);
                }
                if (c == '"') {
                    if (!start) throw new IOException("Quote bukan di awal field.");
                    quoted = true;
                    start = false;
                } else {
                    value.append(c);
                    start = false;
                }
            }
        }

        private String[] finish(String[] fields, int count, StringBuilder value) throws IOException {
            if (count >= 16) throw new IOException("Kolom melebihi 16.");
            fields[count++] = value.toString();
            String[] result = new String[count];
            for (int i = 0; i < count; i++) result[i] = fields[i];
            return result;
        }
    }
}