package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.dao.EditoraDAO;
import com.example.SistemaBiblioteca.model.Editora;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditoraFormController {

    @FXML private TextField txtNome;
    @FXML private TextField txtContato;
    @FXML private TextField txtEndereco;

    private final EditoraDAO dao = new EditoraDAO();
    private Editora editando = null;

    public void setEditora(Editora e) {
        this.editando = e;
        txtNome.setText(e.getNome());
        txtContato.setText(e.getContato());
        txtEndereco.setText(e.getEndereco());
    }

    @FXML
    private void onSalvar() {
        String nome = txtNome.getText();
        if (nome == null || nome.isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Nome é obrigatório").show();
            return;
        }

        if (editando == null) {
            Editora nova = new Editora(null, nome.trim(), txtContato.getText(), txtEndereco.getText());
            boolean ok = dao.insert(nova);
            if (!ok) {
                new Alert(Alert.AlertType.ERROR, "Erro ao cadastrar editora").show();
                return;
            }
        } else {
            editando.setNome(nome.trim());
            editando.setContato(txtContato.getText());
            editando.setEndereco(txtEndereco.getText());
            boolean ok = dao.update(editando);
            if (!ok) {
                new Alert(Alert.AlertType.ERROR, "Erro ao atualizar editora").show();
                return;
            }
        }

        // fecha modal
        ((Stage) txtNome.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelar() {
        ((Stage) txtNome.getScene().getWindow()).close();
    }
}
