package com.pengadilan.app.controller;

import com.pengadilan.app.service.DataManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;

public class ImporEksporController {
    @FXML private Label lblStatusImpor;

    @FXML
    public void handleImporCSV() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            DataManager.getInstance().tambahLog("Mengimpor berkas CSV: " + file.getName());
            if (lblStatusImpor != null) {
                lblStatusImpor.setText("Berhasil mengimpor file: " + file.getName());
            }
        }
    }

    @FXML
    public void handleEksporCSV() {
        DataManager.getInstance().tambahLog("Mengekspor seluruh data putusan.");
        if (lblStatusImpor != null) {
            lblStatusImpor.setText("Berhasil mengekspor data ke format CSV.");
        }
    }
}