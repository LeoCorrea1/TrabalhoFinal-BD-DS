package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DashboardController {

    @FXML
    public void onGerenciarAcervo() {
        // mais tarde trocar para a tela de listagem de acervo
        SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
    }

    @FXML
    public void onEmprestimos() {
        SceneManager.show("emprestimo_list.fxml","Empréstimos");;
    }

    @FXML
    public void onUsuarios() {
        SceneManager.show("usuario_list.fxml", "Usuários");;
    }

    @FXML
    public void onReservas() {
        SceneManager.show("reserva_list.fxml", "Reservas");
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
