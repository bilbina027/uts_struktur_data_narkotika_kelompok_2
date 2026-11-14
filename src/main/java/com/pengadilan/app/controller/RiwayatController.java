package com.pengadilan.app.controller;

import com.pengadilan.app.model.AktivitasLog;
import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.DynamicArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class RiwayatController {
    @FXML private ListView<String> listRiwayat;
    @FXML private Label lblJumlahLog;

    @FXML
    public void initialize() {
        handleRefresh();
    }

    @FXML
    public void handleRefresh() {
        DynamicArray<AktivitasLog> records = DataManager.getInstance().getRiwayat();
        ObservableList<String> adapter = FXCollections.observableArrayList();
        for (int i = 0; i < records.size(); i++) {
            adapter.add(records.get(i).toString());
        }
        listRiwayat.setItems(adapter);
        lblJumlahLog.setText("Jumlah log: " + records.size() + " | Terbaru ditampilkan paling atas.");
    }
}