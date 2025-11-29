package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.LocalizacaoDAO;
import com.example.SistemaBiblioteca.model.Localizacao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LocalizacaoFormController {

    @FXML private TextField txtSetor;
    @FXML private TextField txtPrateleira;
    @FXML private TextField txtCaixa;

    private final LocalizacaoDAO dao = new LocalizacaoDAO();
    private Localizacao editing = null;

    public void setEditing(Localizacao l) {
        this.editing = l;
        if (l == null) return;
        txtSetor.setText(l.getSetor());
        txtPrateleira.setText(l.getPrateleira());
        txtCaixa.setText(l.getCaixa());
    }

    @FXML
    public void onVoltar() {
        SceneManager.show("localizacao_list.fxml", "Localizações");
    }
    @FXML
    public void onSalvar() {
        String setor = txtSetor.getText();
        if (setor == null || setor.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Setor é obrigatório");
            return;
        }

        Localizacao l = (editing != null) ? editing : new Localizacao();
        l.setSetor(setor.trim());
        l.setPrateleira(emptyToNull(txtPrateleira.getText()));
        l.setCaixa(emptyToNull(txtCaixa.getText()));

        boolean ok = (editing == null) ? dao.insert(l) : dao.update(l);
        if (!ok) {
            showAlert(Alert.AlertType.ERROR, "Erro ao salvar localização");
            return;
        }
        // fecha modal
        ((Stage) txtSetor.getScene().getWindow()).close();
    }

    @FXML
    public void onCancelar() {
        ((Stage) txtSetor.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType t, String msg) {
        Alert a = new Alert(t, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}
