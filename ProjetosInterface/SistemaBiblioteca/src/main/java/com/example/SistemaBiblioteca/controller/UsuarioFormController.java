package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.UsuarioDAO;
import com.example.SistemaBiblioteca.model.Usuario;
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
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private final UsuarioDAO dao = new UsuarioDAO();

    @FXML
    public void initialize() {

        comboTipo.getItems().addAll("tecnico", "funcionário", "aluno", "professor");

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

        Usuario u = (usuarioSelecionado == null ? new Usuario() : usuarioSelecionado);

        u.setNome(txtNome.getText());
        u.setEmail(txtEmail.getText());
        u.setTelefone(txtTelefone.getText());
        u.setTipoUsuario(comboTipo.getValue());

        if (usuarioSelecionado == null) dao.insert(u);
        else dao.update(u);

        usuarioSelecionado = null;
        fechar();
    }
    @FXML public void onVoltar() { SceneManager.show("usuario_list.fxml","Usuários"); }

    @FXML
    private void onCancelar() {
        usuarioSelecionado = null;
        fechar();
    }

    private void fechar() {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }
}
