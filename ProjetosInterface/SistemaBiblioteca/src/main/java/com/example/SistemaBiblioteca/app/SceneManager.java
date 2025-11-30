package com.example.SistemaBiblioteca.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * SceneManager atualizado para PRESERVAR estado maximizado/fullscreen entre trocas de cena.
 *
 * Regras importantes:
 * - Chame SceneManager.setPrimaryStage(primaryStage) em Main.start(stage).
 * - Evite definir tamanho fixo em controllers (setWidth/setHeight) ao trocar a cena principal.
 */
public final class SceneManager {

    private static Stage primaryStage = null;

    private SceneManager() {}

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // --- utilitários para carregar FXML ---
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

        // fallback: pega qualquer window visível
        for (javafx.stage.Window w : javafx.stage.Window.getWindows()) {
            if (w.isShowing() && w instanceof Stage) return (Stage) w;
        }
        // última opção: cria uma stage nova
        return new Stage();
    }

    /**
     * Mostra uma cena na stage principal (reaproveita primaryStage quando disponível).
     * Preserva maximized/fullScreen.
     */
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

            // aplica a cena na mesma stage
            stage.setScene(scene);
            if (title != null) stage.setTitle(title);

            // NÃO sobrepor tamanho fixo: se estava maximizado, mantém; se estava fullScreen mantém.
            stage.setMaximized(wasMaximized);
            stage.setFullScreen(wasFullScreen);

            // Se quiser centralizar quando NÃO estiver maximizado, faça opcional:
            if (!wasMaximized && !wasFullScreen) {
                stage.centerOnScreen();
            }

            // mostra (se já estava visível, trocar cena não altera visual)
            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException("FXML not found or error loading: /fxml/" + fxmlName, ex);
        }
    }

    /**
     * Abre um modal (Stage com Modality.APPLICATION_MODAL) e passa o controller ao consumer.
     * Se a primaryStage estiver maximizada, opcionalmente maximiza o modal também (configurável aqui).
     */
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
                // try to set owner to any visible window
                for (javafx.stage.Window w : javafx.stage.Window.getWindows()) {
                    if (w.isShowing() && w instanceof Stage) {
                        modal.initOwner((Stage) w);
                        break;
                    }
                }
            }

            // Preserve maximized/fullscreen of owner if desired:
            boolean ownerMax = primaryStage != null && primaryStage.isMaximized();
            boolean ownerFS = primaryStage != null && primaryStage.isFullScreen();

            modal.setTitle(title == null ? "" : title);
            modal.setScene(new Scene(root));

            // Se o usuário estiver com a app maximizada, maximize o modal também para manter aparência.
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
