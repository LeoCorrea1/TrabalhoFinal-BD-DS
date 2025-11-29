package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DashboardController {

    @FXML
    public void onGerenciarAcervo() {

        SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
    }

    @FXML
    public void onEmprestimos() {
        System.out.println("Abrir tela de Empréstimos (a implementar)");
    }

    @FXML
    public void onUsuarios() {
        System.out.println("Abrir tela de Usuários (a implementar)");
    }

    @FXML
    public void onSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja realmente sair do sistema?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                Platform.exit();
            }
        });
    }

}
