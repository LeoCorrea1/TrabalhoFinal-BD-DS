package com.example.demo.controller;

import com.example.demo.dao.LivroDAO;
import com.example.demo.model.Livro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LivroController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAno;

    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtAssunto;

    @FXML
    private TableView<Livro> tabelaLivros;

    @FXML
    private TableColumn<Livro, Integer> colId;

    @FXML
    private TableColumn<Livro, String> colTitulo;

    @FXML
    private TableColumn<Livro, Integer> colAno;

    @FXML
    private TableColumn<Livro, String> colIsbn;

    @FXML
    private TableColumn<Livro, Integer> colAutor;

    @FXML
    private TableColumn<Livro, Integer> colAssunto;

    private LivroDAO livroDAO = new LivroDAO();
    private ObservableList<Livro> listaLivros;

    @FXML
    public void initialize() {
        carregarColunas();
        carregarTabela();
    }

    private void carregarColunas() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colAno.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAnoPublicacao()).asObject());
        colIsbn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
        colAutor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdAutor()).asObject());
        colAssunto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdAssunto()).asObject());
    }

    private void carregarTabela() {
        listaLivros = FXCollections.observableArrayList(livroDAO.listarTodos());
        tabelaLivros.setItems(listaLivros);
    }

    @FXML
    public void salvarLivro() {

        Livro livro = new Livro();
        livro.setTitulo(txtTitulo.getText());
        livro.setAnoPublicacao(Integer.parseInt(txtAno.getText()));
        livro.setIsbn(txtISBN.getText());
        livro.setIdAutor(Integer.parseInt(txtAutor.getText()));
        livro.setIdAssunto(Integer.parseInt(txtAssunto.getText()));

        livroDAO.inserir(livro);
        carregarTabela();
    }

    @FXML
    public void deletarLivro() {
        Livro livro = tabelaLivros.getSelectionModel().getSelectedItem();

        if (livro != null) {
            livroDAO.deletar(livro.getId());
            carregarTabela();
        }
    }

    @FXML
    public void atualizarLivro() {
        Livro livro = tabelaLivros.getSelectionModel().getSelectedItem();

        if (livro != null) {
            livro.setTitulo(txtTitulo.getText());
            livro.setAnoPublicacao(Integer.parseInt(txtAno.getText()));
            livro.setIsbn(txtISBN.getText());
            livro.setIdAutor(Integer.parseInt(txtAutor.getText()));
            livro.setIdAssunto(Integer.parseInt(txtAssunto.getText()));

            livroDAO.atualizar(livro);
            carregarTabela();
        }
    }
}
