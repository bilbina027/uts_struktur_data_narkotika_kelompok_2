package com.pengadilan.app.model;

import java.time.LocalDate;

public final class Putusan {
    private final String caseId;
    private final String nomorPerkara;
    private final String pengadilan;
    private final LocalDate tanggalPutusan;
    private final String terdakwaKode;
    private final String jenisNarkotika;
    private final double beratBarangBuktiGram;
    private final String pasalDakwaan;
    private final String pasalTerbukti;
    private final int tuntutanBulan;
    private final int vonisBulan;
    private final long dendaRupiah;
    private final String barangBukti;
    private final String keadaanMemberatkan;
    private final String keadaanMeringankan;
    private final String sourcePages;

    public static String[] headers() {
        return new String[]{
                "caseId", "nomorPerkara", "pengadilan", "tanggalPutusan",
                "terdakwaKode", "jenisNarkotika", "beratBarangBuktiGram",
                "pasalDakwaan", "pasalTerbukti", "tuntutanBulan",
                "vonisBulan", "dendaRupiah", "barangBukti",
                "keadaanMemberatkan", "keadaanMeringankan", "sourcePages"
        };
    }

    public Putusan(
            String caseId, String nomorPerkara, String pengadilan,
            LocalDate tanggalPutusan, String terdakwaKode,
            String jenisNarkotika, double beratBarangBuktiGram,
            String pasalDakwaan, String pasalTerbukti,
            int tuntutanBulan, int vonisBulan, long dendaRupiah,
            String barangBukti, String keadaanMemberatkan,
            String keadaanMeringankan, String sourcePages) {
        this.caseId = required(caseId, "caseId");
        this.nomorPerkara = required(nomorPerkara, "nomorPerkara");
        this.pengadilan = required(pengadilan, "pengadilan");
        this.terdakwaKode = required(terdakwaKode, "terdakwaKode");
        this.jenisNarkotika = required(jenisNarkotika, "jenisNarkotika");
        this.sourcePages = required(sourcePages, "sourcePages");
        if (tanggalPutusan == null) {
            throw new IllegalArgumentException("Tanggal wajib diisi.");
        }
        if (!Double.isFinite(beratBarangBuktiGram)
                || beratBarangBuktiGram < 0
                || tuntutanBulan < 0 || vonisBulan < 0 ||
                dendaRupiah < 0) {
            throw new IllegalArgumentException(
                    "Angka harus finite dan tidak negatif.");
        }
        this.tanggalPutusan = tanggalPutusan;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.pasalDakwaan = optional(pasalDakwaan);
        this.pasalTerbukti = optional(pasalTerbukti);
        this.barangBukti = optional(barangBukti);
        this.keadaanMemberatkan = optional(keadaanMemberatkan);
        this.keadaanMeringankan = optional(keadaanMeringankan);
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " wajib diisi.");
        }
        return value.trim();
    }

    private static String optional(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String text(String value) {
        return value == null ? "" : value;
    }

    public String getCaseId() { return caseId; }
    public String getNomorPerkara() { return nomorPerkara; }
    public String getPengadilan() { return pengadilan; }
    public LocalDate getTanggalPutusan() { return tanggalPutusan; }
    public String getTerdakwaKode() { return terdakwaKode; }
    public String getJenisNarkotika() { return jenisNarkotika; }
    public double getBeratBarangBuktiGram() { return beratBarangBuktiGram; }
    public String getPasalDakwaan() { return pasalDakwaan; }
    public String getPasalTerbukti() { return pasalTerbukti; }
    public int getTuntutanBulan() { return tuntutanBulan; }
    public int getVonisBulan() { return vonisBulan; }
    public long getDendaRupiah() { return dendaRupiah; }
    public String getBarangBukti() { return barangBukti; }
    public String getKeadaanMemberatkan() { return keadaanMemberatkan; }
    public String getKeadaanMeringankan() { return keadaanMeringankan; }
    public String getSourcePages() { return sourcePages; }

    public String[] toFields() {
        return new String[]{
                caseId, nomorPerkara, pengadilan, tanggalPutusan.toString(),
                terdakwaKode, jenisNarkotika,
                Double.toString(beratBarangBuktiGram),
                text(pasalDakwaan), text(pasalTerbukti),
                Integer.toString(tuntutanBulan),
                Integer.toString(vonisBulan), Long.toString(dendaRupiah),
                text(barangBukti), text(keadaanMemberatkan),
                text(keadaanMeringankan), sourcePages
        };
    }

    public static Putusan fromFields(String[] fields) {
        if (fields == null || fields.length != 16) {
            throw new IllegalArgumentException("Harus tepat 16 kolom.");
        }
        try {
            return new Putusan(
                    fields[0], fields[1], fields[2],
                    LocalDate.parse(fields[3].trim()), fields[4], fields[5],
                    blank(fields[6]) ? 0 : Double.parseDouble(fields[6].trim()),
                    fields[7], fields[8],
                    blank(fields[9]) ? 0 : Integer.parseInt(fields[9].trim()),
                    blank(fields[10]) ? 0 : Integer.parseInt(fields[10].trim()),
                    blank(fields[11]) ? 0 : Long.parseLong(fields[11].trim()),
                    fields[12], fields[13], fields[14], fields[15]);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(
                    "Record tidak valid: " + exception.getMessage(), exception);
        }
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }
}