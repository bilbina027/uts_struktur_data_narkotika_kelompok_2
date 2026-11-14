package com.pengadilan.app.controller;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {
    @FXML private Label lblTotalPutusan;
    @FXML private Label lblRataVonis;
    @FXML private Label lblTotalDenda;
    @FXML private Label lblTanggalHariIni;
    @FXML private PieChart chartNarkotika;
    @FXML private BarChart<String, Number> chartVonis;

    @FXML
    public void initialize() {
        String tgl = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", new Locale("id", "ID")));
        if (lblTanggalHariIni != null) {
            lblTanggalHariIni.setText(tgl);
        }

        var observableList = DataManager.getInstance().getListPutusan();

        if (lblTotalPutusan != null) {
            lblTotalPutusan.setText(String.valueOf(observableList.size()));
        }

        double avgVonis = observableList.stream().mapToInt(Putusan::getVonisBulan).average().orElse(0.0);
        if (lblRataVonis != null) {
            lblRataVonis.setText(String.format("%.1f Bulan", avgVonis));
        }

        long sumDenda = observableList.stream().mapToLong(Putusan::getDendaRupiah).sum();
        if (lblTotalDenda != null) {
            lblTotalDenda.setText(String.format("Rp %,d", sumDenda));
        }

        // Pengisian Data PieChart (menggunakan String langsung)
        if (chartNarkotika != null) {
            Map<String, Long> counts = observableList.stream()
                    .collect(Collectors.groupingBy(Putusan::getJenisNarkotika, Collectors.counting()));
            chartNarkotika.getData().clear();
            counts.forEach((jenis, count) -> {
                chartNarkotika.getData().add(new PieChart.Data(jenis, count));
            });

            for (PieChart.Data slice : chartNarkotika.getData()) {
                Tooltip tooltip = new Tooltip(slice.getName() + "\nJumlah Kasus: " + (int) slice.getPieValue());
                tooltip.setStyle("-fx-font-size: 13px; -fx-background-color: #1e293b; -fx-text-fill: white; -fx-padding: 6; -fx-background-radius: 4;");
                if (slice.getNode() != null) {
                    slice.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, e -> Tooltip.install(slice.getNode(), tooltip));
                }
            }
        }

        // Pengisian Data BarChart
        if (chartVonis != null) {
            chartVonis.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Vonis (Bulan)");
            Map<String, Double> avgVonisMap = observableList.stream()
                    .collect(Collectors.groupingBy(Putusan::getJenisNarkotika, Collectors.averagingInt(Putusan::getVonisBulan)));
            avgVonisMap.forEach((jenis, avg) -> {
                series.getData().add(new XYChart.Data<>(jenis, avg));
            });
            chartVonis.getData().add(series);
            chartVonis.setLegendVisible(false);
        }
    }

    @FXML
    public void handleKelolaData() {
        pindahHalaman("/com/pengadilan/app/fxml/DataPutusanView.fxml");
    }

    @FXML
    public void handleBukaFilter() {
        pindahHalaman("/com/pengadilan/app/fxml/FilterView.fxml");
    }

    @FXML
    public void handleImporBerkas() {
        pindahHalaman("/com/pengadilan/app/fxml/ImporEksporView.fxml");
    }

    private void pindahHalaman(String fxmlPath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlPath));
            if (chartNarkotika != null && chartNarkotika.getScene() != null) {
                StackPane contentArea = (StackPane) chartNarkotika.getScene().lookup("#contentArea");
                if (contentArea != null) {
                    contentArea.getChildren().setAll(node);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}