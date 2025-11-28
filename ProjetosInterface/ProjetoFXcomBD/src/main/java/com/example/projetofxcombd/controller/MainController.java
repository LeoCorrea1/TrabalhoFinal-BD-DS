package com.example.projetofxcombd.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    public void abrirItemAcervo() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/projetofxcombd/view/ItemAcervoView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Gerenciar Itens do Acervo");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fechar() {
        System.exit(0);
    }
}
