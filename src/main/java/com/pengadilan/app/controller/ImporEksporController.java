package com.pengadilan.app.controller;

import com.pengadilan.app.service.CsvService;
import com.pengadilan.app.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.File;

public class ImporEksporController {
    @FXML private TextField txtKota;
    @FXML private Label lblStatusImpor;

    private final DataManager manager = DataManager.getInstance();
    private final CsvService csvService = new CsvService();

    @FXML
    public void initialize() {
        lblStatusImpor.setText(
                "Data utama: " + manager.size()
                        + " record. Impor membutuhkan 50 putusan nyata."
        );
    }

    @FXML
    public void handleImporCSV() {
        String kota = txtKota.getText();
        if (kota == null || kota.isBlank()) {
            manager.tambahLog("Impor gagal: kota sumber belum diisi.", false);
            lblStatusImpor.setText("Masukkan kota sumber terlebih dahulu, misalnya Malang.");
            return;
        }

        File file = MainController.chooseFile(
                lblStatusImpor.getScene().getWindow(),
                false,
                "Impor 50 Putusan CSV",
                "csv"
        );
        if (file == null) {
            return;
        }

        try {
            CsvService.ImportResult result = csvService.importCsv(file);
            if (!result.acceptable()) {
                String error = result.errorText();
                manager.tambahLog("Impor CSV gagal: " + error, false);
                lblStatusImpor.setText(error + "\nData lama tetap dipertahankan.");
                return;
            }
            manager.replaceAll(result.records(), kota);
            lblStatusImpor.setText(
                    "Impor berhasil: 50 record dari kota " + kota.trim()
                            + ". Pastikan provenance sudah diverifikasi."
            );
        } catch (Exception exception) {
            manager.tambahLog("Impor gagal: " + exception.getMessage(), false);
            lblStatusImpor.setText(
                    "Impor ditolak: " + exception.getMessage()
                            + "\nData lama tetap dipertahankan."
            );
        }
    }

    @FXML
    public void handleEksporCSV() {
        export(false);
    }

    @FXML
    public void handleEksporLaporan() {
        export(true);
    }

    private void export(boolean laporan) {
        File file = MainController.chooseFile(
                lblStatusImpor.getScene().getWindow(),
                true,
                laporan ? "Ekspor Laporan Putusan" : "Ekspor Seluruh Data Putusan",
                laporan ? "txt" : "csv"
        );
        if (file == null) {
            return;
        }
        try {
            if (laporan) {
                csvService.exportReport(file, manager.getDataPutusan());
            } else {
                csvService.exportCsv(file, manager.getDataPutusan());
            }
            manager.tambahLog("Ekspor berhasil: " + file.getName(), true);
            lblStatusImpor.setText("Berkas tersimpan: " + file.getAbsolutePath());
        } catch (Exception exception) {
            manager.tambahLog("Ekspor gagal: " + exception.getMessage(), false);
            lblStatusImpor.setText("Ekspor gagal: " + exception.getMessage());
        }
    }
}