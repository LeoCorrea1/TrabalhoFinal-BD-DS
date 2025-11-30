package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.ExemplarDAO;
import com.example.SistemaBiblioteca.dao.LocalizacaoDAO;
import com.example.SistemaBiblioteca.model.Exemplar;
import com.example.SistemaBiblioteca.model.Localizacao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class ExemplarFormController {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtEstado;
    @FXML private CheckBox chkDisponivel;
    @FXML private ComboBox<Localizacao> comboLocalizacao;

    private final ExemplarDAO dao = new ExemplarDAO();
    private final LocalizacaoDAO localDao = new LocalizacaoDAO();

    private Exemplar editing = null;
    private Integer idItemAcervo = null;

    @FXML
    public void initialize() {
        // carrega localizacoes inicialmente
        refreshLocalizacoes();
    }
    public void setItemAcervo(int idItem) {
        this.idItemAcervo = idItem;
    }
    public void setEditing(Exemplar e) {
        this.editing = e;
        if (e == null) return;

        refreshLocalizacoes();

        txtCodigo.setText(nullSafe(e.getCodigoBarras()));
        txtEstado.setText(nullSafe(e.getEstadoConservacao()));
        chkDisponivel.setSelected(Boolean.TRUE.equals(e.getDisponivel()));

        if (e.getIdLocalizacao() != null && comboLocalizacao.getItems() != null) {
            comboLocalizacao.getItems().stream()
                    .filter(l -> l.getIdLocalizacao() != null && l.getIdLocalizacao().equals(e.getIdLocalizacao()))
                    .findFirst()
                    .ifPresent(comboLocalizacao::setValue);
        }

        this.idItemAcervo = e.getIdItemAcervo();
    }
    @FXML
    public void onSalvar() {
        String codigo = txtCodigo.getText();
        if (codigo == null || codigo.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Código de barras é obrigatório");
            return;
        }
        Exemplar e = (editing != null) ? editing : new Exemplar();
        e.setCodigoBarras(codigo.trim());
        e.setEstadoConservacao(emptyToNull(txtEstado.getText()));
        e.setDisponivel(chkDisponivel.isSelected());

        Localizacao sel = comboLocalizacao.getValue();
        e.setIdLocalizacao(sel == null ? null : sel.getIdLocalizacao());

        if (editing == null) {
            // criação exige id do item
            if (idItemAcervo == null) {
                showAlert(Alert.AlertType.ERROR, "ID do item acervo não informado");
                return;
            }
            e.setIdItemAcervo(idItemAcervo);
            boolean ok = dao.insert(e);
            if (!ok) {
                showAlert(Alert.AlertType.ERROR, "Erro ao inserir exemplar (verifique código de barras duplicado ou restrições).");
                return;
            }
        } else {
            boolean ok = dao.update(e);
            if (!ok) {
                showAlert(Alert.AlertType.ERROR, "Erro ao atualizar exemplar.");
                return;
            }
        }
        Stage st = (Stage) txtCodigo.getScene().getWindow();
        st.close();
    }
    @FXML
    public void onVoltar() {
        SceneManager.show("exemplar_list.fxml", "Exemplares");
    }
    @FXML
    public void onCancelar() {
        Stage st = (Stage) txtCodigo.getScene().getWindow();
        st.close();
    }

    @FXML
    public void onOpenLocalizacao() {
        SceneManager.showModalWithController("localizacao_form.fxml", "Localização", controller -> {
        });

        refreshLocalizacoes();
    }

    private void refreshLocalizacoes() {
        List<Localizacao> lista = localDao.findAll();
        comboLocalizacao.setItems(FXCollections.observableArrayList(lista));
    }

    // utilitários
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

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
