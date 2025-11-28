package com.example.SistemaBiblioteca.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.setPrimaryStage(stage);
        SceneManager.show("login.fxml", "Login");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
