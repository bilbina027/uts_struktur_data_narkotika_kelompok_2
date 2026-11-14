package com.pengadilan.app.service;

import com.pengadilan.app.model.AktivitasLog;
import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.util.DynamicArray;
import com.pengadilan.app.util.CustomStack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class DataManager {
    private static DataManager instance;
    private final DynamicArray<Putusan> dataPutusan = new
            DynamicArray<>();
    private final CustomStack<AktivitasLog> riwayatStack =
            new CustomStack<>();
    private final ObservableList<Putusan> observableList =
            FXCollections.observableArrayList();

    private DataManager() {
        muat50DataPutusan();
        syncObservableList();
        tambahLog("Sistem berhasil memuat 50 data putusan PN Surabaya.");
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public DynamicArray<Putusan> getDataPutusan() {
        return dataPutusan;
    }

    public ObservableList<Putusan> getListPutusan() {
        return observableList;
    }

    public CustomStack<AktivitasLog> getRiwayatStack() {
        return riwayatStack;
    }

    public void tambahPutusan(Putusan p) {
        dataPutusan.add(p);
        if (!dataPutusan.validateInvariant()) {
            throw new IllegalStateException("Invariant DynamicArray rusak setelah penambahan!");
        }
        syncObservableList();
        tambahLog("Menambahkan putusan perkara: " + p.getNomorPerkara());
    }

    public void hapusPutusan(int index) {
        Putusan p = dataPutusan.remove(index);
        if (!dataPutusan.validateInvariant()) {
            throw new IllegalStateException("Invariant DynamicArray rusak setelah penghapusan!");
        }
        syncObservableList();
        tambahLog("Menghapus putusan perkara: " + p.getNomorPerkara());
    }

    public void replaceAll(DynamicArray<Putusan> newStaging) {
        if (!newStaging.validateInvariant()) {
            throw new IllegalStateException("Staging invariant tidak valid.");
        }
        dataPutusan.clear();
        for (int i = 0; i < newStaging.size(); i++) {
            dataPutusan.add(newStaging.get(i));
        }
        syncObservableList();
        tambahLog("Memperbarui data utama dari file impor.");
    }

    public void tambahLog(String deskripsi) {
        riwayatStack.push(new AktivitasLog(deskripsi));
    }

    private void syncObservableList() {
        observableList.clear();
        for (int i = 0; i < dataPutusan.size(); i++) {
            observableList.add(dataPutusan.get(i));
        }
    }

    private void muat50DataPutusan() {
        for (int i = 1; i <= 50; i++) {
            String id = String.format("PN-SBY-%03d", i);
            String noPerkara = (100 + i) + "/Pid.Sus/2024/PN Sby";
            dataPutusan.add(new Putusan(
                    id, noPerkara, "Pengadilan Negeri Surabaya", LocalDate.of(2024, 5, 15),
                    "Terdakwa-" + i, i % 2 == 0 ? "Shabu / Metamfetamina" : "Ganja / Kanabis", 1.5 * i, "Pasal 114 ayat (1)", "Pasal 114 ayat (1)", 60, 48 + (i % 12), 500000000L * (i % 3 + 1), "Barang bukti narkotika seberat " + (1.5 * i) + " gram", "Meresahkan masyarakat", "Berlaku sopan di persidangan", "20, 21"));
        }
    }
}
