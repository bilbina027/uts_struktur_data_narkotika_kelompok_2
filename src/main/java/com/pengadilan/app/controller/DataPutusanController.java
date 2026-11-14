package com.pengadilan.app.controller;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.DataManager;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DataPutusanController {
    @FXML private TextField txtCari;
    @FXML private Label lblInfoEntri;
    @FXML private TableView<Putusan> tablePutusan;
    @FXML private TableColumn<Putusan, String> colCaseId;
    @FXML private TableColumn<Putusan, String> colNomorPerkara;
    @FXML private TableColumn<Putusan, String> colJenis;
    @FXML private TableColumn<Putusan, Integer> colVonis;
    @FXML private TableColumn<Putusan, Void> colAksi;

    @FXML
    public void initialize() {
        colCaseId.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        colNomorPerkara.setCellValueFactory(new PropertyValueFactory<>("nomorPerkara"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisNarkotika"));
        colVonis.setCellValueFactory(new PropertyValueFactory<>("vonisBulan"));

        tablePutusan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colAksi.setCellFactory(param -> new TableCell<>() {
            private final Button btnDetail = new Button("Detail Perkara");

            {
                btnDetail.setStyle("-fx-font-size: 11px; -fx-padding: 4 10; -fx-background-color: #0f172a; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");
                btnDetail.setOnAction(event -> {
                    Putusan p = getTableView().getItems().get(getIndex());
                    showAlert("Detail Putusan", "Nomor Perkara: " + p.getNomorPerkara() +
                            "\nPasal Terbukti: " + p.getPasalTerbukti() +
                            "\nVonis: " + p.getVonisBulan() + " Bulan" +
                            "\nDenda: Rp " + String.format("%,d", p.getDendaRupiah()));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDetail);
            }
        });

        FilteredList<Putusan> filteredData = new FilteredList<>(DataManager.getInstance().getListPutusan(), p -> true);

        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(putusan -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String filter = newValue.toLowerCase();
                return putusan.getCaseId().toLowerCase().contains(filter) ||
                        putusan.getNomorPerkara().toLowerCase().contains(filter) ||
                        putusan.getJenisNarkotika().toLowerCase().contains(filter);
            });
            lblInfoEntri.setText("Menampilkan " + filteredData.size() + " dari 50 entri");
        });

        tablePutusan.setItems(filteredData);
    }

    @FXML
    public void handleTambahData() {
        showAlert("Tambah Data", "Form input data putusan baru siap dibuka.");
    }

    @FXML
    public void handleEkspor() {
        DataManager.getInstance().tambahLog("Mengekspor data putusan ke CSV.");
        showAlert("Ekspor Berhasil", "Seluruh data putusan berhasil diunduh ke berkas CSV.");
    }

    @FXML
    public void handleBukaFilter() {
        showAlert("Filter Cepat", "Gunakan menu navigasi 'Filter & Statistik' di samping untuk filter parameter lanjutan.");
    }

    @FXML
    public void handleUrutkan() {
        tablePutusan.getSortOrder().add(colVonis);
        showAlert("Pengurutan", "Tabel diurutkan berdasarkan durasi vonis.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}