package com.pengadilan.app.service;

import com.pengadilan.app.model.AktivitasLog;
import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.util.CustomStack;
import com.pengadilan.app.util.DynamicArray;
import java.math.BigInteger;
import java.util.Locale;

public final class DataManager {
    private static final DataManager INSTANCE = new DataManager();
    private DynamicArray<Putusan> data = new DynamicArray<>();
    private final CustomStack<AktivitasLog> logs = new CustomStack<>();
    private DynamicArray<String> trace = new DynamicArray<>();

    private DataManager() {
        tambahLog("Aplikasi siap; belum ada dataset.", true);
    }

    public static DataManager getInstance() { return INSTANCE; }

    public DynamicArray<Putusan> getDataPutusan() { return data.copy(); }

    public int size() { return data.size(); }

    public void tambahLog(String description, boolean success) {
        logs.push(new AktivitasLog(description, success));
    }

    public void tambahLog(String description) { tambahLog(description, true); }

    public DynamicArray<AktivitasLog> getRiwayat() {
        DynamicArray<AktivitasLog> result = new DynamicArray<>();
        for (int i = 0; i < logs.size(); i++) {
            result.add(logs.getFromTop(i));
        }
        return result;
    }

    public boolean validateInvariant() {
        return data.validateInvariant() && logs.validateInvariant() && trace.validateInvariant();
    }

    static void ensureUnique(DynamicArray<Putusan> existing, Putusan candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Record tidak boleh null.");
        }
        for (int i = 0; i < existing.size(); i++) {
            Putusan current = existing.get(i);
            if (current.getCaseId().equalsIgnoreCase(candidate.getCaseId())) {
                throw new IllegalArgumentException("Duplikasi caseId: " + candidate.getCaseId());
            }
            if (current.getNomorPerkara().equalsIgnoreCase(candidate.getNomorPerkara())) {
                throw new IllegalArgumentException("Duplikasi nomor perkara: " + candidate.getNomorPerkara());
            }
        }
    }

    private static String normalisasiPengadilan(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nama Pengadilan Negeri wajib diisi.");
        }
        return name.trim().replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
    }

    public void tambahPutusan(Putusan record) {
        try {
            if (record == null) {
                throw new IllegalArgumentException("Record tidak boleh null.");
            }
            if (!data.isEmpty()) {
                String existingCourt = normalisasiPengadilan(data.get(0).getPengadilan());
                String newCourt = normalisasiPengadilan(record.getPengadilan());
                if (!existingCourt.equals(newCourt)) {
                    throw new IllegalArgumentException(
                            "Putusan harus berasal dari Pengadilan Negeri " + data.get(0).getPengadilan()
                    );
                }
            }
            ensureUnique(data, record);
            data.add(record);
            tambahLog("Tambah: " + record.getCaseId(), true);
        } catch (RuntimeException exception) {
            tambahLog("Tambah gagal: " + exception.getMessage(), false);
            throw exception;
        }
    }

    public boolean hapusPutusanByCaseId(String caseId) {
        if (caseId == null || caseId.isBlank()) {
            tambahLog("Hapus gagal: caseId kosong.", false);
            throw new IllegalArgumentException("caseId wajib diisi.");
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getCaseId().equalsIgnoreCase(caseId.trim())) {
                int shifted = data.size() - i - 1;
                data.remove(i);
                tambahLog("Hapus " + caseId + "; indeks=" + i + "; elemen bergeser=" + shifted, true);
                return true;
            }
        }
        tambahLog("Hapus gagal: " + caseId + " tidak ditemukan.", false);
        return false;
    }

    public record SearchResult(int index, int steps, Putusan putusan) {
        public boolean found() { return index >= 0; }
    }

    public SearchResult cariPerkara(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            tambahLog("Cari gagal: keyword kosong.", false);
            throw new IllegalArgumentException("Keyword wajib diisi.");
        }
        String query = keyword.trim().toLowerCase(Locale.ROOT);
        int steps = 0;
        for (int i = 0; i < data.size(); i++) {
            steps++;
            Putusan record = data.get(i);
            if (record.getCaseId().toLowerCase(Locale.ROOT).contains(query)
                    || record.getNomorPerkara().toLowerCase(Locale.ROOT).contains(query)) {
                tambahLog("Cari ditemukan: " + keyword + "; indeks=" + i + "; langkah=" + steps, true);
                return new SearchResult(i, steps, record);
            }
        }
        tambahLog("Cari tidak ditemukan: " + keyword + "; langkah=" + steps, true);
        return new SearchResult(-1, steps, null);
    }

    public record FilterCriteria(
            String jenis, Double beratMin, Double beratMax,
            String pasal, Integer vonisMin, Integer vonisMax) {
        public FilterCriteria {
            jenis = jenis == null ? "" : jenis.trim();
            pasal = pasal == null ? "" : pasal.trim();
            if ((beratMin != null && (!Double.isFinite(beratMin) || beratMin < 0))
                    || (beratMax != null && (!Double.isFinite(beratMax) || beratMax < 0))) {
                throw new IllegalArgumentException("Rentang berat tidak valid.");
            }
            if (beratMin != null && beratMax != null && beratMin > beratMax) {
                throw new IllegalArgumentException("Berat min melebihi max.");
            }
            if ((vonisMin != null && vonisMin < 0)
                    || (vonisMax != null && vonisMax < 0)
                    || (vonisMin != null && vonisMax != null && vonisMin > vonisMax)) {
                throw new IllegalArgumentException("Rentang vonis tidak valid.");
            }
        }

        public boolean matches(Putusan p) {
            return (jenis.isBlank() || p.getJenisNarkotika().equalsIgnoreCase(jenis))
                    && (beratMin == null || p.getBeratBarangBuktiGram() >= beratMin)
                    && (beratMax == null || p.getBeratBarangBuktiGram() <= beratMax)
                    && (pasal.isBlank() || (p.getPasalTerbukti() != null
                    && p.getPasalTerbukti().toLowerCase(Locale.ROOT).contains(pasal.toLowerCase(Locale.ROOT))))
                    && (vonisMin == null || p.getVonisBulan() >= vonisMin)
                    && (vonisMax == null || p.getVonisBulan() <= vonisMax);
        }
    }

    public DynamicArray<Putusan> filter(FilterCriteria criteria) {
        if (criteria == null) {
            tambahLog("Filter gagal: kriteria null.", false);
            throw new IllegalArgumentException("Kriteria wajib diisi.");
        }
        DynamicArray<Putusan> result = new DynamicArray<>();
        for (int i = 0; i < data.size(); i++) {
            if (criteria.matches(data.get(i))) result.add(data.get(i));
        }
        tambahLog("Filter selesai: " + result.size() + " hasil.", true);
        return result;
    }

    public void replaceAll(DynamicArray<Putusan> staging) {
        try {
            if (staging == null || staging.size() != 50 || !staging.validateInvariant()) {
                throw new IllegalArgumentException("Impor harus berisi tepat 50 record valid.");
            }
            String targetPengadilan = normalisasiPengadilan(staging.get(0).getPengadilan());
            DynamicArray<Putusan> replacement = new DynamicArray<>(50);
            for (int i = 0; i < staging.size(); i++) {
                Putusan putusan = staging.get(i);
                ensureUnique(replacement, putusan);
                String currentPengadilan = normalisasiPengadilan(putusan.getPengadilan());
                if (!targetPengadilan.equals(currentPengadilan)) {
                    throw new IllegalArgumentException(
                            "Record " + putusan.getCaseId() + " berasal dari " + putusan.getPengadilan()
                                    + ", sedangkan seluruh dataset harus berasal dari " + staging.get(0).getPengadilan()
                    );
                }
                replacement.add(putusan);
            }
            data = replacement;
            tambahLog("Impor berhasil: 50 putusan dari " + staging.get(0).getPengadilan() + ".", true);
        } catch (RuntimeException exception) {
            tambahLog("Impor ditolak; data lama tetap dipertahankan: " + exception.getMessage(), false);
            throw exception;
        }
    }

    public void urutkanBerdasarkanVonis() {
        trace = new DynamicArray<>();
        trace.add("Awal: " + orderText());
        long comparisons = 0;
        long shifts = 0;
        for (int i = 1; i < data.size(); i++) {
            Putusan key = data.get(i);
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (data.get(j).getVonisBulan() <= key.getVonisBulan()) {
                    break;
                }
                data.set(j + 1, data.get(j));
                shifts++;
                j--;
            }
            data.set(j + 1, key);
            trace.add("i=" + i + "; key=" + key.getCaseId() + "; posisi=" + (j + 1) + "; " + orderText());
        }
        trace.add("Perbandingan=" + comparisons + "; pergeseran=" + shifts);
        trace.add("Invariant=" + data.validateInvariant());
        tambahLog("Insertion Sort selesai; perbandingan=" + comparisons + "; pergeseran=" + shifts, true);
    }

    private String orderText() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < data.size(); i++) {
            if (i > 0) result.append(", ");
            result.append(data.get(i).getCaseId()).append(':').append(data.get(i).getVonisBulan());
        }
        return result.append(']').toString();
    }

    public DynamicArray<String> getSortTrace() { return trace.copy(); }

    public record Ringkasan(int jumlah, double rataVonis, BigInteger totalDenda) {}

    public Ringkasan hitungRingkasan() { return summarize(data); }

    public static Ringkasan summarize(DynamicArray<Putusan> records) {
        long totalVonis = 0;
        BigInteger totalDenda = BigInteger.ZERO;
        for (int i = 0; i < records.size(); i++) {
            Putusan p = records.get(i);
            totalVonis += p.getVonisBulan();
            totalDenda = totalDenda.add(BigInteger.valueOf(p.getDendaRupiah()));
        }
        return new Ringkasan(
                records.size(),
                records.isEmpty() ? 0 : (double) totalVonis / records.size(),
                totalDenda
        );
    }

    public record DistribusiJenis(String jenis, int jumlah) {}

    public DynamicArray<DistribusiJenis> distribusiJenis() {
        DynamicArray<DistribusiJenis> result = new DynamicArray<>();
        for (int i = 0; i < data.size(); i++) {
            String jenis = data.get(i).getJenisNarkotika();
            int index = -1;
            for (int j = 0; j < result.size(); j++) {
                if (result.get(j).jenis().equalsIgnoreCase(jenis)) {
                    index = j;
                    break;
                }
            }
            if (index < 0) {
                result.add(new DistribusiJenis(jenis, 1));
            } else {
                DistribusiJenis previous = result.get(index);
                result.set(index, new DistribusiJenis(previous.jenis(), previous.jumlah() + 1));
            }
        }
        return result;
    }
}