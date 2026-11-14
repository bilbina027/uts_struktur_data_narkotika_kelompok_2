package com.pengadilan.app.controller;

import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.DynamicArray;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.util.Locale;

public class DashboardController {
    @FXML private Label lblTotalPerkara;
    @FXML private Label lblRataVonis;
    @FXML private Label lblTotalDenda;
    @FXML private PieChart chartNarkotika;
    @FXML private BarChart<String, Number> chartVonis;

    private MainController mainController;

    public void setMainController(MainController controller) {
        mainController = controller;
    }

    @FXML
    public void initialize() {
        DataManager manager = DataManager.getInstance();
        DataManager.Ringkasan summary = manager.hitungRingkasan();

        lblTotalPerkara.setText(Integer.toString(summary.jumlah()));
        lblRataVonis.setText(String.format(Locale.ROOT, "%.2f bulan", summary.rataVonis()));
        lblTotalDenda.setText("Rp " + summary.totalDenda());

        DynamicArray<DataManager.DistribusiJenis> distribution = manager.distribusiJenis();
        chartNarkotika.getData().clear();
        for (int i = 0; i < distribution.size(); i++) {
            DataManager.DistribusiJenis item = distribution.get(i);
            chartNarkotika.getData().add(new PieChart.Data(item.jenis(), item.jumlah()));
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rata-rata vonis");
        series.getData().add(new XYChart.Data<>("Seluruh perkara", summary.rataVonis()));
        chartVonis.getData().setAll(series);
        chartVonis.setLegendVisible(false);
    }

    @FXML
    public void handleKelolaData() {
        if (mainController != null) mainController.bukaDataPutusan();
    }

    @FXML
    public void handleBukaFilter() {
        if (mainController != null) mainController.bukaFilter();
    }

    @FXML
    public void handleImporBerkas() {
        if (mainController != null) mainController.bukaImporEkspor();
    }
}
