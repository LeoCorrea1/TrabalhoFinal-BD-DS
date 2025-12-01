package com.example.SistemaAcervo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    // CORREÇÃO: Use barra inicial e caminho correto
    private static final String resourcePath = "/fxml/acervo.fxml";

    @Override
    public void start(Stage stage) {
        try {
            URL fxmlUrl = getClass().getResource(resourcePath);
            System.out.println("Procurando FXML em: " + resourcePath + " -> " + fxmlUrl);

            if (fxmlUrl == null) {
                // Tentativa alternativa
                InputStream is = getClass().getResourceAsStream(resourcePath);
                System.err.println("getResource returned null. getResourceAsStream -> " + (is != null));
                throw new RuntimeException("FXML não encontrado no classpath: " + resourcePath
                        + "\nVerifique se o arquivo está em src/main/resources/fxml/ e se foi copiado para target/classes.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Sistema de Acervo");
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}