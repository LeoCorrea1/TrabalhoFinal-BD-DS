package com.example.SistemaBiblioteca.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    public void abrirItemAcervo() {
        abrirTela("/com/example/SistemaBiblioteca/view/itemAcervoView.fxml", "Item Acervo");
    }

    public void abrirLivro() {
        abrirTela("/com/example/SistemaBiblioteca/view/livroView.fxml", "Livro");
    }

    public void fecharApp() {
        System.exit(0);
    }

    private void abrirTela(String caminho, String titulo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminho));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir tela: " + e.getMessage());
        }
    }
}
