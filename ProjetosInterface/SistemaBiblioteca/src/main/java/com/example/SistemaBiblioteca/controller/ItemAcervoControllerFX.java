package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.repositor.ItemAcervoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ItemAcervoControllerFX {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtSubtitulo;
    @FXML private TextField txtAno;
    @FXML private TextField txtIdioma;
    @FXML private TextArea txtDescricao;

    @FXML private TableView<ItemAcervo> tabela;
    @FXML private TableColumn<ItemAcervo, Integer> colId;
    @FXML private TableColumn<ItemAcervo, String> colTitulo;
    @FXML private TableColumn<ItemAcervo, String> colIdioma;

    private ItemAcervoDAO dao = new ItemAcervoDAO();
    private ObservableList<ItemAcervo> lista;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(a -> a.getValue().idProperty().asObject());
        colTitulo.setCellValueFactory(a -> a.getValue().tituloProperty());
        colIdioma.setCellValueFactory(a -> a.getValue().idiomaProperty());

        carregarTabela();
    }

    private void carregarTabela() {
        lista = FXCollections.observableArrayList(dao.listarTodos());
        tabela.setItems(lista);
    }

    @FXML
    private void salvar() {
        try {
            ItemAcervo item = new ItemAcervo();
            item.setTitulo(txtTitulo.getText());
            item.setSubtitulo(txtSubtitulo.getText());
            item.setAno(Integer.parseInt(txtAno.getText()));
            item.setIdioma(txtIdioma.getText());
            item.setDescricao(txtDescricao.getText());

            dao.inserir(item);

            mostrarInfo("Item cadastrado!");
            carregarTabela();
            limpar();

        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void atualizar() {
        ItemAcervo selecionado = tabela.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarErro("Selecione um item!");
            return;
        }

        try {
            selecionado.setTitulo(txtTitulo.getText());
            selecionado.setSubtitulo(txtSubtitulo.getText());
            selecionado.setAno(Integer.parseInt(txtAno.getText()));
            selecionado.setIdioma(txtIdioma.getText());
            selecionado.setDescricao(txtDescricao.getText());

            dao.atualizar(selecionado);

            mostrarInfo("Item atualizado!");
            carregarTabela();
            limpar();

        } catch (Exception e) {
            mostrarErro("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void deletar() {
        ItemAcervo selecionado = tabela.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarErro("Selecione um item!");
            return;
        }

        dao.deletar(selecionado.getId());
        mostrarInfo("Item removido!");

        carregarTabela();
        limpar();
    }

    @FXML
    private void selecionarDaTabela() {
        ItemAcervo item = tabela.getSelectionModel().getSelectedItem();
        if (item == null) return;

        txtTitulo.setText(item.getTitulo());
        txtSubtitulo.setText(item.getSubtitulo());
        txtAno.setText(String.valueOf(item.getAno()));
        txtIdioma.setText(item.getIdioma());
        txtDescricao.setText(item.getDescricao());
    }

    private void limpar() {
        txtTitulo.clear();
        txtSubtitulo.clear();
        txtAno.clear();
        txtIdioma.clear();
        txtDescricao.clear();
    }

    private void mostrarErro(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
