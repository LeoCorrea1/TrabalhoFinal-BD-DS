package com.example.SistemaBiblioteca.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public final class SceneManager {

    private static Stage primaryStage = null;

    private SceneManager() {}

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static FXMLLoader loadFXML(String fxmlName) {
        String path = "/fxml/" + fxmlName;
        URL resource = SceneManager.class.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("FXML not found: " + path);
        }
        return new FXMLLoader(resource);
    }

    private static Stage obtainStage() {
        if (primaryStage != null) return primaryStage;
        for (javafx.stage.Window w : javafx.stage.Window.getWindows()) {
            if (w.isShowing() && w instanceof Stage) return (Stage) w;
        }
        return new Stage();
    }

    public static void show(String fxmlName, String title) {
        show(fxmlName, title, null);
    }

    public static void show(String fxmlName, String title, Consumer<FXMLLoader> afterLoad) {
        try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();

            // IMPORTANTE: Chamar o consumer DEPOIS do load()
            if (afterLoad != null) {
                afterLoad.accept(loader);
            }

            Stage stage = obtainStage();
            boolean wasMaximized = stage.isMaximized();
            boolean wasFullScreen = stage.isFullScreen();

            stage.setScene(new Scene(root));
            if (title != null) stage.setTitle(title);
            stage.setMaximized(wasMaximized);
            stage.setFullScreen(wasFullScreen);
            if (!wasMaximized && !wasFullScreen) stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException("Error loading: /fxml/" + fxmlName, ex);
        }
    }

    public static void showModalWithController(String fxmlName, String title, Consumer<Object> controllerConsumer) {
        try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();
            Object controller = loader.getController();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);

            if (primaryStage != null) {
                modal.initOwner(primaryStage);
            }

            modal.setTitle(title == null ? "" : title);
            modal.setScene(new Scene(root));

            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            modal.showAndWait();
        } catch (IOException ex) {
            throw new RuntimeException("Error loading modal: /fxml/" + fxmlName, ex);
        }
    }

    public static void showModal(String fxmlName, String title) {
        showModalWithController(fxmlName, title, null);
    }
}