package com.pengadilan.app.controller;

import com.pengadilan.app.model.JenisNarkotika;
import com.pengadilan.app.model.Putusan;
import com.pengadilan.app.service.CsvService;
import com.pengadilan.app.service.DataManager;
import com.pengadilan.app.util.DynamicArray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.File;

public class DataPutusanController {
    @FXML private TableView<Putusan> tablePutusan;
    @FXML private TextField txtCari;
    @FXML private Label lblStatus;

    private final DataManager manager = DataManager.getInstance();
    private final CsvService csvService = new CsvService();
    private DynamicArray<Putusan> displayed = new DynamicArray<>();

    @FXML
    public void initialize() {
        MainController.configureTable(tablePutusan);
        handleRefresh();
    }

    private void display(DynamicArray<Putusan> rows) {
        displayed = rows.copy();
        MainController.showRows(tablePutusan, displayed);
    }

    @FXML
    public void handleRefresh() {
        display(manager.getDataPutusan());
        lblStatus.setText("Jumlah data: " + displayed.size());
    }

    @FXML
    public void handleCari() {
        try {
            DataManager.SearchResult result = manager.cariPerkara(txtCari.getText());
            DynamicArray<Putusan> rows = new DynamicArray<>();
            if (result.found()) rows.add(result.putusan());
            display(rows);
            lblStatus.setText(
                    (result.found() ? "Ditemukan" : "Tidak ditemukan")
                            + " | indeks utama=" + result.index()
                            + " | langkah=" + result.steps());
        } catch (Exception exception) {
            MainController.error(exception);
        }
    }

    @FXML
    public void handleUrutkan() {
        manager.urutkanBerdasarkanVonis();
        handleRefresh();
        lblStatus.setText("Insertion Sort stabil selesai.");
    }

    @FXML
    public void handleHapus() {
        Putusan selected = tablePutusan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            manager.tambahLog("Hapus gagal: belum memilih record.", false);
            MainController.error(new IllegalArgumentException("Pilih record dahulu."));
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus " + selected.getCaseId() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        if (manager.hapusPutusanByCaseId(selected.getCaseId())) {
            handleRefresh();
        } else {
            MainController.error(new IllegalArgumentException("Record tidak ditemukan."));
        }
    }

    @FXML
    public void handleTambah() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initOwner(tablePutusan.getScene().getWindow());
        dialog.setTitle("Tambah Putusan Terverifikasi");

        String[] headers = Putusan.headers();
        TextField[] fields = new TextField[16];
        ComboBox<String> jenis = new ComboBox<>();
        jenis.setEditable(true);
        jenis.setPrefWidth(420);
        for (JenisNarkotika value : JenisNarkotika.values()) {
            jenis.getItems().add(value.getLabel());
        }

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(8);
        grid.setPadding(new Insets(12));
        for (int i = 0; i < 16; i++) {
            fields[i] = new TextField();
            fields[i].setPrefWidth(420);
            grid.add(new Label(headers[i]), 0, i);
            grid.add(i == 5 ? jenis : fields[i], 1, i);
        }

        fields[3].setPromptText("YYYY-MM-DD");
        fields[4].setPromptText("Kode anonim");
        fields[6].setPromptText("Gram; titik desimal");
        fields[15].setPromptText("Halaman sumber tiap kelompok atribut");

        Label error = new Label();
        error.setWrapText(true);
        error.setStyle("-fx-text-fill: #b91c1c;");

        ScrollPane scroll = new ScrollPane(new VBox(8, grid, error));
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(550);
        dialog.getDialogPane().setContent(scroll);

        ButtonType save = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(save, ButtonType.CANCEL);
        Button saveButton = (Button) dialog.getDialogPane().lookupButton(save);

        saveButton.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            Putusan record;
            try {
                String[] values = new String[16];
                for (int i = 0; i < 16; i++) values[i] = fields[i].getText();
                values[5] = jenis.getEditor().getText();
                record = Putusan.fromFields(values);
            } catch (Exception exception) {
                manager.tambahLog("Form tambah ditolak: " + exception.getMessage(), false);
                error.setText(exception.getMessage());
                return;
            }
            try {
                manager.tambahPutusan(record);
                dialog.close();
                handleRefresh();
            } catch (Exception exception) {
                error.setText(exception.getMessage());
            }
        });

        dialog.showAndWait();
    }

    @FXML
    public void handleEkspor() {
        File file = MainController.chooseFile(
                tablePutusan.getScene().getWindow(),
                true, "Ekspor tampilan CSV", "csv");
        if (file == null) return;
        try {
            csvService.exportCsv(file, displayed);
            manager.tambahLog("Ekspor tampilan: " + displayed.size() + " record.", true);
            lblStatus.setText("CSV tersimpan: " + file.getAbsolutePath());
        } catch (Exception exception) {
            manager.tambahLog("Ekspor gagal: " + exception.getMessage(), false);
            MainController.error(exception);
        }
    }

    @FXML
    public void handleEksporTrace() {
        File file = MainController.chooseFile(
                tablePutusan.getScene().getWindow(),
                true, "Ekspor trace sorting", "txt");
        if (file == null) return;
        try {
            csvService.exportTrace(file, manager.getSortTrace());
            manager.tambahLog("Ekspor trace sorting.", true);
            lblStatus.setText("Trace tersimpan: " + file.getAbsolutePath());
        } catch (Exception exception) {
            manager.tambahLog("Ekspor trace gagal: " + exception.getMessage(), false);
            MainController.error(exception);
        }
    }
}
