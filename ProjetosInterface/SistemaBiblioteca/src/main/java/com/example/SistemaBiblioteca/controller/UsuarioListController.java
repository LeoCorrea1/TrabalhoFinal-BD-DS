package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.UsuarioDAO;
import com.example.SistemaBiblioteca.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuarioListController {

    @FXML private TableView<Usuario> tabela;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colTelefone;
    @FXML private TableColumn<Usuario, String> colTipo;
    @FXML private TextField txtPesquisa;

    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private final UsuarioDAO dao = new UsuarioDAO();
    private ObservableList<Usuario> lista;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(f -> new javafx.beans.property.SimpleIntegerProperty(f.getValue().getIdUsuario()).asObject());
        colNome.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getNome()));
        colEmail.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getEmail()));
        colTelefone.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getTelefone()));
        colTipo.setCellValueFactory(f -> new javafx.beans.property.SimpleStringProperty(f.getValue().getTipoUsuario()));

        carregarTabela();

        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            boolean sel = newV != null;
            btnEditar.setDisable(!sel);
            btnExcluir.setDisable(!sel);
        });
    }

    private void carregarTabela() {
        lista = FXCollections.observableArrayList(dao.findAll());
        tabela.setItems(lista);
    }
    @FXML public void onVoltar() { SceneManager.show("dashboard.fxml","Painel"); }

    @FXML
    private void onPesquisar() {
        String q = txtPesquisa.getText().toLowerCase();

        tabela.setItems(lista.filtered(u ->
                u.getNome().toLowerCase().contains(q) ||
                        u.getEmail().toLowerCase().contains(q)
        ));
    }
    @FXML
    private void onNovo() {
        UsuarioFormController.usuarioSelecionado = null;
        SceneManager.showModal("usuario_form.fxml", "Novo Usuário");
        carregarTabela();
    }
    @FXML
    private void onEditar() {
        Usuario u = tabela.getSelectionModel().getSelectedItem();
        if (u == null) return;

        UsuarioFormController.usuarioSelecionado = u;
        SceneManager.showModal("usuario_form.fxml", "Editar Usuário");
        carregarTabela();
    }
    @FXML
    private void onExcluir() {
        Usuario u = tabela.getSelectionModel().getSelectedItem();
        if (u == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir usuário " + u.getNome() + " ?", ButtonType.YES, ButtonType.NO);

        if (a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            dao.delete(u.getIdUsuario());
            carregarTabela();
        }
    }
}
