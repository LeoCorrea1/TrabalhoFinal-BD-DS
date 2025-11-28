package com.example.projetofxcombd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projetofxcombd/view/MainView.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Biblioteca - Sistema de Acervo");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar a interface gráfica.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
