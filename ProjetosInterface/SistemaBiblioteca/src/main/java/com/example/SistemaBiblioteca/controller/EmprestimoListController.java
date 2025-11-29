package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EmprestimoDAO;
import com.example.SistemaBiblioteca.model.Emprestimo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmprestimoListController {

    @FXML private TableView<Emprestimo> table;
    @FXML private TableColumn<Emprestimo, Integer> colId;
    @FXML private TableColumn<Emprestimo, String> colExemplar;
    @FXML private TableColumn<Emprestimo, String> colUsuario;
    @FXML private TableColumn<Emprestimo, String> colEmprestimo;
    @FXML private TableColumn<Emprestimo, String> colPrevista;
    @FXML private TableColumn<Emprestimo, String> colDevolucao;
    @FXML private TableColumn<Emprestimo, String> colStatus;

    private final EmprestimoDAO dao = new EmprestimoDAO();
    private final ObservableList<Emprestimo> data = FXCollections.observableArrayList();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmprestimo"));
        colExemplar.setCellValueFactory(new PropertyValueFactory<>("tituloExemplar"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colEmprestimo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataEmprestimo() == null ? "" : fmt.format(c.getValue().getDataEmprestimo())));
        colPrevista.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataPrevistaDevolucao() == null ? "" : fmt.format(c.getValue().getDataPrevistaDevolucao())));
        colDevolucao.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataDevolucao() == null ? "" : fmt.format(c.getValue().getDataDevolucao())));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(data);
        carregar();
    }

    private void carregar() {
        Task<List<Emprestimo>> t = new Task<>() {
            @Override protected List<Emprestimo> call() { return dao.findAllComNomes(); }
        };
        t.setOnSucceeded(e -> data.setAll(t.getValue()));
        new Thread(t).start();
    }

    @FXML private void onAtualizar() { carregar(); }

    @FXML private void onNovo()  { SceneManager.show("emprestimo_form.fxml", "Novo Empréstimo"); }

    @FXML
    private void onEditar() {
        Emprestimo sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Selecione um registro."); return; }
        SceneManager.show("emprestimo_form.fxml", "Editar Empréstimo", loader -> {
            EmprestimoFormController ctrl = loader.getController();
            ctrl.setEmprestimo(sel);
        });
    }

    @FXML
    private void onExcluir() {
        Emprestimo sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Selecione um empréstimo."); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Excluir este empréstimo?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait();
        if (a.getResult() == ButtonType.YES) {
            new Thread(() -> {
                dao.delete(sel.getIdEmprestimo());
                carregar();
            }).start();
        }
    }
    @FXML public void onVoltar() { SceneManager.show("dashboard.fxml","Painel"); }

    private void showError(String msg) { Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).showAndWait()); }
}