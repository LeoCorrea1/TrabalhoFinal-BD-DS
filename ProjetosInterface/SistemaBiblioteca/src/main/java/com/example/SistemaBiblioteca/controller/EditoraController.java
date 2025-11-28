package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EditoraDAO;
import com.example.SistemaBiblioteca.model.Editora;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditoraController {

    @FXML private TableView<Editora> tableView;
    @FXML private TableColumn<Editora, Integer> colId;
    @FXML private TableColumn<Editora, String> colNome;
    @FXML private TableColumn<Editora, String> colContato;
    @FXML private TableColumn<Editora, String> colEndereco;

    @FXML private TextField searchField;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private final EditoraDAO dao = new EditoraDAO();
    private ObservableList<Editora> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // cell factories simples
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue().getId() == null ? 0 : c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getNome() == null ? "" : c.getValue().getNome()));
        colContato.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getContato() == null ? "" : c.getValue().getContato()));
        colEndereco.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getEndereco() == null ? "" : c.getValue().getEndereco()));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            boolean sel = n != null;
            if (btnEditar != null) btnEditar.setDisable(!sel);
            if (btnExcluir != null) btnExcluir.setDisable(!sel);
        });

        carregarDados();
    }

    @FXML
    public void onRefresh() { carregarDados(); }

    @FXML
    public void onPesquisar() { carregarDados(); }

    @FXML
    public void onNova() {
        // abre modal para criar; recarrega após fechar
        SceneManager.showModalWithController("editoras_form.fxml", "Nova Editora", controller -> {});
        carregarDados();
    }

    @FXML
    public void onEditar() {
        Editora sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        SceneManager.showModalWithController("editoras_form.fxml", "Editar Editora", controller -> {
            if (controller instanceof EditoraFormController ctrl) {
                ctrl.setEditora(sel);
            }
        });
        carregarDados();
    }

    @FXML
    public void onExcluir() {
        Editora sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirma exclusão da editora \"" + sel.getNome() + "\"?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                boolean ok = dao.delete(sel.getId());
                if (ok) carregarDados();
                else new Alert(Alert.AlertType.ERROR, "Não foi possível excluir (existem dependências).").show();
            }
        });
    }

    private void carregarDados() {
        Task<ObservableList<Editora>> task = new Task<>() {
            @Override
            protected ObservableList<Editora> call() {
                return FXCollections.observableArrayList(dao.search(searchField.getText() == null ? "" : searchField.getText()));
            }
        };
        task.setOnSucceeded(e -> {
            data = task.getValue();
            tableView.setItems(data);
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao carregar editoras: " + task.getException().getMessage()).show();
        });
        new Thread(task).start();
    }
}
