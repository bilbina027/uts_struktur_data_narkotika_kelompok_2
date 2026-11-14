package com.pengadilan.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL view = MainApp.class.getResource("/com/pengadilan/app/fxml/MainView.fxml");
        URL css = MainApp.class.getResource("/com/pengadilan/app/css/style.css");
        if (view == null || css == null) {
            throw new IllegalStateException("MainView.fxml atau style.css tidak ditemukan.");
        }
        Parent root = FXMLLoader.load(view);
        Scene scene = new Scene(root, 1250, 800);
        scene.getStylesheets().add(css.toExternalForm());
        stage.setTitle("UTS Struktur Data Putusan Narkotika");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}