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
            throw new IllegalArgumentException("FXML not found on classpath: " + path);
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
        show(fxmlName, title, (loader) -> {});
    }
    public static void show(String fxmlName, String title, Consumer<FXMLLoader> afterLoad) {
        try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();
            Stage stage = obtainStage();
            boolean wasMaximized = stage.isMaximized();
            boolean wasFullScreen = stage.isFullScreen();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            if (title != null) stage.setTitle(title);
            stage.setMaximized(wasMaximized);
            stage.setFullScreen(wasFullScreen);
            if (!wasMaximized && !wasFullScreen) {
                stage.centerOnScreen();
            }
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException("FXML not found or error loading: /fxml/" + fxmlName, ex);
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
            } else {
                for (javafx.stage.Window w : javafx.stage.Window.getWindows()) {
                    if (w.isShowing() && w instanceof Stage) {
                        modal.initOwner((Stage) w);
                        break;
                    }
                }
            }
            boolean ownerMax = primaryStage != null && primaryStage.isMaximized();
            boolean ownerFS = primaryStage != null && primaryStage.isFullScreen();

            modal.setTitle(title == null ? "" : title);
            modal.setScene(new Scene(root));
            if (ownerMax) modal.setMaximized(true);
            if (ownerFS) modal.setFullScreen(true);
            if (controllerConsumer != null) controllerConsumer.accept(controller);

            modal.showAndWait();
        } catch (IOException ex) {
            throw new RuntimeException("FXML not found or error loading modal: /fxml/" + fxmlName, ex);
        }
    }
    public static void showModal(String fxmlName, String title) {
        showModalWithController(fxmlName, title, (c) -> {});
    }
}
