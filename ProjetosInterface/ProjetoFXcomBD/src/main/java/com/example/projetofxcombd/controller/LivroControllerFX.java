package com.example.projetofxcombd.controller;

import com.example.projetofxcombd.model.ItemAcervo;
import com.example.projetofxcombd.model.Livro;
import com.example.projetofxcombd.repositor.ItemAcervoDAO;
import com.example.projetofxcombd.repositor.LivroDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LivroControllerFX {

    @FXML private TextField txtIsbn;
    @FXML private TextField txtEdicao;
    @FXML private TextField txtPaginas;
    @FXML private TextField txtIdEditora;

    @FXML private ComboBox<ItemAcervo> comboAcervo;

    private ItemAcervoDAO acervoDAO = new ItemAcervoDAO();
    private LivroDAO livroDAO = new LivroDAO();

    @FXML
    public void initialize() {
        comboAcervo.setItems(FXCollections.observableArrayList(acervoDAO.listarTodos()));
    }

    @FXML
    private void salvar() {
        try {
            ItemAcervo item = comboAcervo.getValue();
            if (item == null) {
                mostrarErro("Selecione um ItemAcervo!");
                return;
            }

            Livro livro = new Livro();
            livro.setId(item.getId());
            livro.setIsbn(txtIsbn.getText());
            livro.setEdicao(txtEdicao.getText());
            livro.setNumeroPaginas(Integer.parseInt(txtPaginas.getText()));
            livro.setIdEditora(Integer.parseInt(txtIdEditora.getText()));

            livroDAO.inserir(livro);

            mostrarInfo("Livro cadastrado com sucesso!");
            limpar();

        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void buscarPorISBN() {
        Livro livro = livroDAO.buscarPorISBN(txtIsbn.getText());

        if (livro == null) {
            mostrarErro("Livro não encontrado.");
            return;
        }

        preencherCampos(livro);
    }

    @FXML
    private void buscarPorID() {
        try {
            int id = comboAcervo.getValue().getId();
            Livro livro = livroDAO.buscarPorId(id);

            if (livro == null) {
                mostrarErro("Livro não encontrado para este ItemAcervo.");
                return;
            }

            preencherCampos(livro);

        } catch (Exception e) {
            mostrarErro("Erro: " + e.getMessage());
        }
    }

    private void preencherCampos(Livro livro) {
        txtIsbn.setText(livro.getIsbn());
        txtEdicao.setText(livro.getEdicao());
        txtPaginas.setText(String.valueOf(livro.getNumeroPaginas()));
        txtIdEditora.setText(String.valueOf(livro.getIdEditora()));
    }

    @FXML
    private void deletar() {
        try {
            int id = comboAcervo.getValue().getId();

            livroDAO.deletar(id);

            mostrarInfo("Livro removido!");
            limpar();

        } catch (Exception e) {
            mostrarErro("Erro ao deletar: " + e.getMessage());
        }
    }

    private void limpar() {
        txtIsbn.clear();
        txtEdicao.clear();
        txtPaginas.clear();
        txtIdEditora.clear();
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
