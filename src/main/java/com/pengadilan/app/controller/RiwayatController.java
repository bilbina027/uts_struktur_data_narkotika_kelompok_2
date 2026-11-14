package com.pengadilan.app.controller;

import com.pengadilan.app.model.AktivitasLog;
import com.pengadilan.app.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class RiwayatController {
    @FXML private ListView<AktivitasLog> listViewAudit;

    @FXML
    public void initialize() {
        var stack = DataManager.getInstance().getRiwayatStack();
        listViewAudit.getItems().clear();

        // Membaca stack kustom secara LIFO menggunakan getFromTop tanpa koleksi terlarang
        int size = stack.size();
        for (int i = 0; i < size; i++) {
            AktivitasLog log = stack.getFromTop(i);
            listViewAudit.getItems().add(log);
        }

        listViewAudit.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AktivitasLog item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox box = new VBox(4);
                    box.setStyle("-fx-padding: 8; -fx-background-color: white; -fx-background-radius: 6; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 4, 0, 0, 1);");
                    Label lblTitle = new Label(item.getDeskripsi());
                    lblTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e293b;");
                    Label lblSub = new Label("Pengguna: " + item.getUser() + " | Waktu: " + item.getWaktuFormatted() + " | Status: " + item.getStatus());
                    lblSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
                    box.getChildren().addAll(lblTitle, lblSub);
                    setGraphic(box);
                }
            }
        });
    }
}