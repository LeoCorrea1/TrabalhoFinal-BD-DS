package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.dao.MovimentacaoDAO;
import com.example.SistemaBiblioteca.model.Movimentacao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MovimentacaoListController {

    @FXML private TableView<Movimentacao> table;
    @FXML private TableColumn<Movimentacao, String> colTipo;
    @FXML private TableColumn<Movimentacao, String> colDescricao;
    @FXML private TableColumn<Movimentacao, String> colUsuario;
    @FXML private TableColumn<Movimentacao, String> colExemplar;
    @FXML private TableColumn<Movimentacao, String> colDataHora;

    private final MovimentacaoDAO dao = new MovimentacaoDAO();
    private final ObservableList<Movimentacao> data = FXCollections.observableArrayList();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colExemplar.setCellValueFactory(new PropertyValueFactory<>("codigoExemplar"));
        colDataHora.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(fmt.format(c.getValue().getDataHora())));
        table.setItems(data);
        carregar();
    }

    private void carregar() {
        Task<List<Movimentacao>> t = new Task<>() {
            @Override protected List<Movimentacao> call() { return dao.findAll(); }
        };
        t.setOnSucceeded(e -> data.setAll(t.getValue()));
        new Thread(t).start();
    }

    @FXML private void onAtualizar(){ carregar(); }

    private void showError(String msg){
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR,msg).showAndWait());
    }
}