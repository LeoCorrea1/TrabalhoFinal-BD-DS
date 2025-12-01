package com.example.SistemaAcervo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuPrincipalController {

    @FXML
    private void onAbrirGaleria(ActionEvent event) {
        abrirTela("/fxml/ListaAcervo.fxml", "Galeria do Acervo");
    }

    @FXML
    private void onAbrirTipos(ActionEvent event) {
        abrirTela("/fxml/TipoItem.fxml", "Tipos de Item");
    }

    @FXML
    private void onAbrirAutores(ActionEvent event) {
        abrirTela("/fxml/ListaAutores.fxml", "Autores");
    }

    @FXML
    private void onAbrirVinculos(ActionEvent event) {
        abrirTela("/fxml/VinculoAutorLivro.fxml", "Vincular Autor a Livro");
    }

    private void abrirTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}