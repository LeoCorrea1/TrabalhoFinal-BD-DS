package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.UsuarioDAO;
import com.example.SistemaBiblioteca.model.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UsuarioFormController {

    public static Usuario usuarioSelecionado;

    @FXML private Label lblTitulo;
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefone;
    @FXML private ComboBox<String> comboTipo;

    private final UsuarioDAO dao = new UsuarioDAO();

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("tecnico", "publico", "aluno", "professor");

        if (usuarioSelecionado != null) {
            lblTitulo.setText("Editar Usuário");
            preencherCampos(usuarioSelecionado);
        } else {
            lblTitulo.setText("Novo Usuário");
        }
    }

    private void preencherCampos(Usuario u) {
        txtNome.setText(u.getNome());
        txtEmail.setText(u.getEmail());
        txtTelefone.setText(u.getTelefone());
        comboTipo.setValue(u.getTipoUsuario());
    }

    @FXML
    private void onSalvar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String tipo = comboTipo.getValue();

        if (nome == null || nome.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Campo Nome é obrigatório!");
            return;
        }
        if (email == null || email.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Campo Email é obrigatório!");
            return;
        }
        if (tipo == null || tipo.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Selecione o tipo de usuário!");
            return;
        }

        Usuario u = (usuarioSelecionado == null ? new Usuario() : usuarioSelecionado);
        u.setNome(nome.trim());
        u.setEmail(email.trim());
        u.setTelefone(txtTelefone.getText());
        u.setTipoUsuario(tipo);

        if (usuarioSelecionado == null) {
            dao.insert(u);
            showAlert(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!");
        } else {
            dao.update(u);
            showAlert(Alert.AlertType.INFORMATION, "Usuário atualizado com sucesso!");
        }

        usuarioSelecionado = null;
        fecharModal();   // fecha corretamente o modal
    }

    @FXML
    public void onVoltar() {
        // apenas fecha o modal — a lista recarrega automaticamente após fechamento
        fecharModal();
    }

    private void fecharModal() {
        Platform.runLater(() -> {
            Stage stage = (Stage) txtNome.getScene().getWindow();
            stage.close();
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(type, msg, ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
        });
    }
}