package com.example.SistemaBiblioteca.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * SceneManager simples e seguro para carregar FXMLs de /fxml/<nome>
 *
 * Uso:
 * - SceneManager.setPrimaryStage(primaryStage)  <-- chame em Main.start(...)
 * - SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
 * - SceneManager.show("exemplar_list.fxml", "Exemplares", loader -> {
 *       ExemplarListController ctrl = loader.getController();
 *       ctrl.setItemAcervoId(id);
 *   });
 *
 * - SceneManager.showModalWithController("editoras_form.fxml", "Nova", controller -> {
 *       if (controller instanceof EditoraFormController c) c.setEditora(ed);
 *   });
 */
public final class SceneManager {

    private static Stage primaryStage = null;
    private static Stage currentModal;

    private SceneManager() {}

    /**
     * Registre a primary stage (recomendo chamar em Main.start(stage))
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Simples: troca a cena principal para o FXML informado.
     */
    public static void show(String fxmlName, String title) {
        show(fxmlName, title, (loader) -> {});
    }

    public static void show(String fxmlName, String title, Consumer<FXMLLoader> afterLoad) {
        try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();

            if (afterLoad != null) afterLoad.accept(loader);

            Scene scene = new Scene(root);

            Stage stage = obtainStage();
            stage.setScene(scene);
            if (title != null) stage.setTitle(title);
            stage.setWidth(900);
            stage.setHeight(600);
            stage.centerOnScreen();

            // intercepta clicar no X
            stage.setOnCloseRequest(e -> {
                e.consume();
                SceneManager.show("dashboard.fxml","Painel");
            });

            stage.show();

        } catch (IOException ex) {
            throw new RuntimeException("FXML not found or error loading: /fxml/" + fxmlName, ex);
        }
    }

    /**
     * Abre um modal (nova Stage) e passa o controller para o consumer após carregar.
     * A diferença entre esse e show(...) é que esse cria uma Stage modal.
     * controllerConsumer recebe o controller (loader.getController()) para facilitar uso.
     */
    public static void showModalWithController(String fxmlName, String title, Consumer<Object> controllerConsumer) {
        try {
            FXMLLoader loader = loadFXML(fxmlName);
            Parent root = loader.load();
            Object controller = loader.getController();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);

            // tenta definir owner se primaryStage estiver setado
            if (primaryStage != null) modal.initOwner(primaryStage);

            modal.setTitle(title == null ? "" : title);
            modal.setScene(new Scene(root));

            if (controllerConsumer != null) controllerConsumer.accept(controller);

            modal.showAndWait();
        } catch (IOException ex) {
            throw new RuntimeException("FXML not found or error loading modal: /fxml/" + fxmlName, ex);
        }
    }

    /**
     * Abre modal simples sem entregar controller.
     */
    public static void showModal(String fxmlName, String title) {

        showModalWithController(fxmlName, title, (c) -> {});
    }

    /**
     * Obtém o FXMLLoader para o arquivo em /fxml/<fxmlName>.
     * NÃO chama loader.load() aqui — o chamador deve carregar para obter controller.
     */
    public static FXMLLoader loadFXML(String fxmlName) {
        String path = "/fxml/" + fxmlName;
        URL resource = SceneManager.class.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("FXML not found on classpath: " + path);
        }
        return new FXMLLoader(resource);
    }

    /**
     * Obtém uma Stage para uso (primary se disponível; se não, tenta usar a janela atual).
     */
    private static Stage obtainStage() {
        if (primaryStage != null) return primaryStage;

        // fallback: pega qualquer window visível e o converte para Stage
        // (útil durante desenvolvimento quando Main não chamou setPrimaryStage)
        for (javafx.stage.Window w : javafx.stage.Window.getWindows()) {
            if (w.isShowing() && w instanceof Stage) return (Stage) w;
        }

        // última opção: cria uma stage nova
        return new Stage();
    }
    public static void closeModal() {
        if (currentModal != null) {
            currentModal.close();
            currentModal = null;
        }
    }

}
