package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.ExemplarDAO;
import com.example.SistemaBiblioteca.model.Exemplar;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class ExemplarListController {

    @FXML private TableView<Exemplar> tableView;
    @FXML private TableColumn<Exemplar,Integer> colId;
    @FXML private TableColumn<Exemplar,String> colCodigo;
    @FXML private TableColumn<Exemplar,String> colEstado;
    @FXML private TableColumn<Exemplar,Boolean> colDisp;
    @FXML private TableColumn<Exemplar,String> colLocal; // agora String

    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private final ExemplarDAO dao = new ExemplarDAO();
    private int idItemAcervo; // chave recebida da tela anterior

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue() == null || c.getValue().getIdExemplar() == null ? 0 : c.getValue().getIdExemplar()).asObject());
        colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getCodigoBarras() == null ? "" : c.getValue().getCodigoBarras()));
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getEstadoConservacao() == null ? "" : c.getValue().getEstadoConservacao()));
        colDisp.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(
                c.getValue() != null && Boolean.TRUE.equals(c.getValue().getDisponivel())).asObject());

        // agora mostra o nome/descrição da localização
        colLocal.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getNomeLocalizacao() == null ? "" : c.getValue().getNomeLocalizacao()));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            boolean sel = n != null;
            if (btnEditar != null) btnEditar.setDisable(!sel);
            if (btnExcluir != null) btnExcluir.setDisable(!sel);
        });
    }

    /** Chamado pelo ItemAcervoController */
    public void setItemAcervoId(int id) {
        this.idItemAcervo = id;
        loadData();
    }

    private void loadData() {
        List<Exemplar> lista = dao.findByItem(idItemAcervo);
        tableView.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    public void onNovo() {
        SceneManager.showModalWithController("exemplar_form.fxml", "Novo Exemplar", controller -> {
            if (controller instanceof ExemplarFormController ctrl) {
                ctrl.setItemAcervo(idItemAcervo);
            }
        });
        loadData();
    }

    @FXML
    public void onEditar() {
        Exemplar ex = tableView.getSelectionModel().getSelectedItem();
        if (ex == null) return;

        SceneManager.showModalWithController("exemplar_form.fxml", "Editar Exemplar", controller -> {
            if (controller instanceof ExemplarFormController ctrl) {
                ctrl.setEditing(ex);
            }
        });
        loadData();
    }

    @FXML
    public void onExcluir() {
        Exemplar ex = tableView.getSelectionModel().getSelectedItem();
        if (ex == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Excluir exemplar?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        if (a.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        dao.delete(ex.getIdExemplar());
        loadData();
    }

    @FXML
    public void onVoltar() {
        SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
    }
}
