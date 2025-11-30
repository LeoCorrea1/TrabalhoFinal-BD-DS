package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.LocalizacaoDAO;
import com.example.SistemaBiblioteca.model.Localizacao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class LocalizacaoListController {

    @FXML private TableView<Localizacao> tableView;
    @FXML private TableColumn<Localizacao, Integer> colId;
    @FXML private TableColumn<Localizacao, String> colSetor;
    @FXML private TableColumn<Localizacao, String> colPrateleira;
    @FXML private TableColumn<Localizacao, String> colCaixa;
    @FXML private TextField searchField;
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;

    private final LocalizacaoDAO dao = new LocalizacaoDAO();
    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(
                c.getValue() == null || c.getValue().getIdLocalizacao() == null ? 0 : c.getValue().getIdLocalizacao()
        ).asObject());
        colSetor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getSetor() == null ? "" : c.getValue().getSetor()
        ));
        colPrateleira.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getPrateleira() == null ? "" : c.getValue().getPrateleira()
        ));
        colCaixa.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue() == null || c.getValue().getCaixa() == null ? "" : c.getValue().getCaixa()
        ));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs,o,n) -> {
            boolean sel = n != null;
            if (btnEditar != null) btnEditar.setDisable(!sel);
            if (btnExcluir != null) btnExcluir.setDisable(!sel);
        });
        loadData();
    }
    //AJUDA DA IA
    private void loadData() {
        List<Localizacao> lista = dao.findAll();
        tableView.setItems(FXCollections.observableArrayList(lista));
    }
    @FXML
    public void onVoltar() { SceneManager.show("dashboard.fxml","Painel"); }


    @FXML
    public void onPesquisar() {
        tableView.setItems(FXCollections.observableArrayList(dao.search(searchField.getText())));
    }
    @FXML
    public void onNovo() {
        SceneManager.showModalWithController("localizacao_form.fxml", "Nova Localização", controller -> {
        });
        loadData();
    }

    @FXML
    public void onEditar() {
        Localizacao l = tableView.getSelectionModel().getSelectedItem();
        if (l == null) return;

        SceneManager.showModalWithController("localizacao_form.fxml", "Editar Localização", controller -> {
            if (controller instanceof LocalizacaoFormController ctrl) {
                ctrl.setEditing(l);
            }
        });
        loadData();
    }
    @FXML
    public void onExcluir() {
        Localizacao l = tableView.getSelectionModel().getSelectedItem();
        if (l == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Excluir localização?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        if (a.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        boolean ok = dao.delete(l.getIdLocalizacao());
        if (!ok) {
            new Alert(Alert.AlertType.ERROR, "Não foi possível excluir (pode haver exemplares vinculados).").showAndWait();
        }
        loadData();
    }
    @FXML
    public void onFechar() {
        SceneManager.show("exemplar_list.fxml", "Exemplares");
    }
}
