package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    public void onEntrar() {
        String email = emailField.getText();
        // para demo inicial: sem autenticação real -> vai direto ao dashboard
        SceneManager.show("dashboard.fxml", "Sistema Biblioteca - Dashboard");
    }
}
