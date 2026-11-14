package com.pengadilan.app.controller;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.DataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FilterController {
    @FXML private ComboBox<String> cbJenisNarkotika;
    @FXML private ComboBox<String> cbTahun;
    @FXML private TextField txtVonisMin;
    @FXML private TextField txtVonisMax;
    @FXML private Label lblCount;
    @FXML private Label lblAvgVonis;
    @FXML private Label lblTotalDenda;
    @FXML private TableView<Putusan> tableFilter;
    @FXML private TableColumn<Putusan, String> colCaseId;
    @FXML private TableColumn<Putusan, String> colNomorPerkara;
    @FXML private TableColumn<Putusan, String> colJenis;
    @FXML private TableColumn<Putusan, Integer> colVonis;

    private ObservableList<Putusan> masterData;

    @FXML
    public void initialize() {
        colCaseId.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        colNomorPerkara.setCellValueFactory(new PropertyValueFactory<>("nomorPerkara"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisNarkotika"));
        colVonis.setCellValueFactory(new PropertyValueFactory<>("vonisBulan"));

        tableFilter.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        masterData = DataManager.getInstance().getListPutusan();

        // Mengisi ComboBox dengan pilihan String jenis narkotika
        cbJenisNarkotika.setItems(FXCollections.observableArrayList(
                "Shabu / Metamfetamina", "Ganja / Kanabis", "Ekstasi / MDMA", "Tembakau Sintetis", "Obat Keras / Pil Trex", "Lainnya"
        ));
        cbTahun.setItems(FXCollections.observableArrayList("2024", "2023", "2022"));

        tampilkanData(masterData);
    }

    @FXML
    public void handleFilter() {
        String selectedJenis = cbJenisNarkotika.getValue();
        double minVonis = parseDouble(txtVonisMin.getText(), 0);
        double maxVonis = parseDouble(txtVonisMax.getText(), Double.MAX_VALUE);

        ObservableList<Putusan> filtered = FXCollections.observableArrayList();
        for (int i = 0; i < masterData.size(); i++) {
            Putusan p = masterData.get(i);
            boolean matchJenis = (selectedJenis == null || selectedJenis.isEmpty() || p.getJenisNarkotika().equalsIgnoreCase(selectedJenis));
            boolean matchVonis = (p.getVonisBulan() >= minVonis && p.getVonisBulan() <= maxVonis);

            if (matchJenis && matchVonis) {
                filtered.add(p);
            }
        }

        tampilkanData(filtered);
        DataManager.getInstance().tambahLog("Menerapkan filter data putusan lanjutan.");
    }

    @FXML
    public void handleReset() {
        cbJenisNarkotika.setValue(null);
        cbTahun.setValue(null);
        txtVonisMin.clear();
        txtVonisMax.clear();
        tampilkanData(masterData);
    }

    private void tampilkanData(ObservableList<Putusan> data) {
        tableFilter.setItems(data);
        lblCount.setText(String.valueOf(data.size()));
        double avgVonis = data.stream().mapToInt(Putusan::getVonisBulan).average().orElse(0.0);
        lblAvgVonis.setText(String.format("%.1f Bulan", avgVonis));
        long sumDenda = data.stream().mapToLong(Putusan::getDendaRupiah).sum();
        lblTotalDenda.setText(String.format("Rp %,d", sumDenda));
    }

    private double parseDouble(String val, double def) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return def;
        }
    }
}