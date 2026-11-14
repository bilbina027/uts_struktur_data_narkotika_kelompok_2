package com.pengadilan.app.controller;

import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.DynamicArray;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.net.URL;

public class MainController {
    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        bukaDashboard();
    }

    @FXML public void bukaDashboard() { load("DashboardView.fxml"); }
    @FXML public void bukaDataPutusan() { load("DataPutusanView.fxml"); }
    @FXML public void bukaFilter() { load("FilterView.fxml"); }
    @FXML public void bukaRiwayat() { load("RiwayatView.fxml"); }
    @FXML public void bukaImporEkspor() { load("ImporEksporView.fxml"); }

    private void load(String name) {
        try {
            URL url = getClass().getResource("/com/pengadilan/app/fxml/" + name);
            if (url == null) {
                throw new IllegalStateException("Resource tidak ditemukan: " + name);
            }
            FXMLLoader loader = new FXMLLoader(url);
            Parent view = loader.load();
            Object controller = loader.getController();
            if (controller instanceof DashboardController dashboard) {
                dashboard.setMainController(this);
            }
            contentArea.getChildren().setAll(view);
        } catch (Exception exception) {
            DataManager.getInstance().tambahLog("Muat halaman gagal: " + exception.getMessage(), false);
            error(exception);
        }
    }

    static void configureTable(TableView<Putusan> table) {
        table.getColumns().clear();
        String[] headers = Putusan.headers();
        for (int i = 0; i < headers.length; i++) {
            final int index = i;
            TableColumn<Putusan, String> column = new TableColumn<>(headers[i]);
            column.setCellValueFactory(cell ->
                    new ReadOnlyStringWrapper(cell.getValue().toFields()[index]));
            column.setSortable(false);
            column.setPrefWidth(i == 1 ? 230 : 150);
            table.getColumns().add(column);
        }
        table.setPlaceholder(new Label("Tidak ada data."));
    }

    static void showRows(TableView<Putusan> table, DynamicArray<Putusan> records) {
        ObservableList<Putusan> adapter = FXCollections.observableArrayList();
        for (int i = 0; i < records.size(); i++) adapter.add(records.get(i));
        table.setItems(adapter);
    }

    static File chooseFile(Window owner, boolean save, String title, String extension) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(extension.toUpperCase(), "*." + extension));
        if (save) chooser.setInitialFileName("putusan." + extension);
        return save ? chooser.showSaveDialog(owner) : chooser.showOpenDialog(owner);
    }

    static void error(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Operasi Ditolak");
        alert.setHeaderText(null);
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }
}