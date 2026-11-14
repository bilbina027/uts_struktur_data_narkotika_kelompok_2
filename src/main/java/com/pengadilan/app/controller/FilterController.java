package com.pengadilan.app.controller;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.CsvService;
import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.DynamicArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.File;
import java.util.Locale;

public class FilterController {
    @FXML private TableView<Putusan> tableFilter;
    @FXML private ComboBox<String> cbJenisNarkotika;
    @FXML private TextField txtBeratMin;
    @FXML private TextField txtBeratMax;
    @FXML private TextField txtPasal;
    @FXML private TextField txtVonisMin;
    @FXML private TextField txtVonisMax;
    @FXML private Label lblHasil;

    private final DataManager manager = DataManager.getInstance();
    private DynamicArray<Putusan> result = new DynamicArray<>();

    @FXML
    public void initialize() {
        MainController.configureTable(tableFilter);
        cbJenisNarkotika.getItems().add("Semua");
        DynamicArray<DataManager.DistribusiJenis> types = manager.distribusiJenis();
        for (int i = 0; i < types.size(); i++) {
            cbJenisNarkotika.getItems().add(types.get(i).jenis());
        }
        handleReset();
    }

    private Double decimal(TextField field) {
        if (field.getText().isBlank()) return null;
        double value = Double.parseDouble(field.getText().trim());
        if (!Double.isFinite(value) || value < 0) {
            throw new IllegalArgumentException("Berat harus finite dan tidak negatif.");
        }
        return value;
    }

    private Integer integer(TextField field) {
        if (field.getText().isBlank()) return null;
        int value = Integer.parseInt(field.getText().trim());
        if (value < 0) {
            throw new IllegalArgumentException("Vonis tidak boleh negatif.");
        }
        return value;
    }

    @FXML
    public void handleFilter() {
        DataManager.FilterCriteria criteria;
        try {
            String jenis = cbJenisNarkotika.getValue();
            criteria = new DataManager.FilterCriteria(
                    jenis == null || jenis.equals("Semua") ? "" : jenis,
                    decimal(txtBeratMin), decimal(txtBeratMax),
                    txtPasal.getText(),
                    integer(txtVonisMin), integer(txtVonisMax));
        } catch (Exception exception) {
            manager.tambahLog("Input filter ditolak: " + exception.getMessage(), false);
            MainController.error(new IllegalArgumentException("Input filter tidak valid: " + exception.getMessage()));
            return;
        }
        result = manager.filter(criteria);
        showResult();
    }

    private void showResult() {
        MainController.showRows(tableFilter, result);
        DataManager.Ringkasan summary = DataManager.summarize(result);
        lblHasil.setText(String.format(Locale.ROOT,
                "Hasil: %d | Rata-rata: %.2f bulan | Total denda: Rp %s",
                summary.jumlah(), summary.rataVonis(), summary.totalDenda()));
    }

    @FXML
    public void handleReset() {
        cbJenisNarkotika.setValue("Semua");
        txtBeratMin.clear();
        txtBeratMax.clear();
        txtPasal.clear();
        txtVonisMin.clear();
        txtVonisMax.clear();
        result = manager.getDataPutusan();
        showResult();
    }

    @FXML
    public void handleEkspor() {
        File file = MainController.chooseFile(
                tableFilter.getScene().getWindow(),
                true, "Ekspor hasil filter", "csv");
        if (file == null) return;
        try {
            new CsvService().exportCsv(file, result);
            manager.tambahLog("Ekspor hasil filter: " + result.size() + " record.", true);
            lblHasil.setText("CSV tersimpan: " + file.getAbsolutePath());
        } catch (Exception exception) {
            manager.tambahLog("Ekspor filter gagal: " + exception.getMessage(), false);
            MainController.error(exception);
        }
    }
}