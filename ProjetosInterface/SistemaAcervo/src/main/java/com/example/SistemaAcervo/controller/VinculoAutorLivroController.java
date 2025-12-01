package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.AutorDao;
import com.example.SistemaAcervo.dao.LivroAutorDao;
import com.example.SistemaAcervo.dao.LivroDao;
import com.example.SistemaAcervo.dao.LivroDao.LivroComTitulo;
import com.example.SistemaAcervo.model.Autor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class VinculoAutorLivroController {

    @FXML private ComboBox<LivroComTitulo> cmbLivros;
    @FXML private ListView<Autor> listAutoresVinculados;
    @FXML private ListView<Autor> listTodosAutores;
    @FXML private ComboBox<String> cmbPapel;

    private final LivroDao livroDao = new LivroDao();
    private final AutorDao autorDao = new AutorDao();
    private final LivroAutorDao livroAutorDao = new LivroAutorDao();

    @FXML
    private void initialize() {
        // Configurar ComboBox de livros
        cmbLivros.setCellFactory(c -> new ListCell<>() {
            @Override
            protected void updateItem(LivroComTitulo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });
        cmbLivros.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LivroComTitulo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });

        // Quando selecionar um livro, carregar seus autores
        cmbLivros.setOnAction(e -> carregarAutoresDoLivro());

        // Papel padrão
        cmbPapel.getSelectionModel().selectFirst();

        // Carregar dados iniciais
        carregarLivros();
        carregarTodosAutores();
    }

    @FXML
    private void onRefresh() {
        carregarLivros();
        carregarTodosAutores();
        carregarAutoresDoLivro();
    }

    private void carregarLivros() {
        try {
            List<LivroComTitulo> livros = livroDao.findAllComTitulo();
            cmbLivros.setItems(FXCollections.observableArrayList(livros));
        } catch (Exception e) {
            showError("Erro ao carregar livros: " + e.getMessage());
        }
    }

    private void carregarTodosAutores() {
        try {
            List<Autor> autores = autorDao.findAll();
            listTodosAutores.setItems(FXCollections.observableArrayList(autores));
        } catch (Exception e) {
            showError("Erro ao carregar autores: " + e.getMessage());
        }
    }

    private void carregarAutoresDoLivro() {
        LivroComTitulo livroSelecionado = cmbLivros.getSelectionModel().getSelectedItem();

        if (livroSelecionado == null) {
            listAutoresVinculados.setItems(FXCollections.emptyObservableList());
            return;
        }

        try {
            List<Autor> autores = livroAutorDao.listarAutoresDoLivro(livroSelecionado.getIdLivro());
            listAutoresVinculados.setItems(FXCollections.observableArrayList(autores));
        } catch (Exception e) {
            showError("Erro ao carregar autores do livro: " + e.getMessage());
        }
    }

    @FXML
    private void onVincular() {
        LivroComTitulo livroSelecionado = cmbLivros.getSelectionModel().getSelectedItem();
        Autor autorSelecionado = listTodosAutores.getSelectionModel().getSelectedItem();
        String papel = cmbPapel.getSelectionModel().getSelectedItem();

        if (livroSelecionado == null) {
            showError("Selecione um livro.");
            return;
        }

        if (autorSelecionado == null) {
            showError("Selecione um autor da lista de disponíveis.");
            return;
        }

        if (papel == null || papel.isEmpty()) {
            papel = "Autor";
        }

        try {
            livroAutorDao.vincularAutor(livroSelecionado.getIdLivro(), autorSelecionado.getIdAutor(), papel);
            showInfo("Autor vinculado com sucesso!");
            carregarAutoresDoLivro();
        } catch (Exception e) {
            showError("Erro ao vincular: " + e.getMessage());
        }
    }

    @FXML
    private void onRemoverVinculo() {
        LivroComTitulo livroSelecionado = cmbLivros.getSelectionModel().getSelectedItem();
        Autor autorSelecionado = listAutoresVinculados.getSelectionModel().getSelectedItem();

        if (livroSelecionado == null) {
            showError("Selecione um livro.");
            return;
        }

        if (autorSelecionado == null) {
            showError("Selecione um autor vinculado para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar");
        confirmacao.setHeaderText("Remover vínculo?");
        confirmacao.setContentText("Remover " + autorSelecionado.getNome() + " do livro?");

        confirmacao.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    livroAutorDao.removerVinculo(livroSelecionado.getIdLivro(), autorSelecionado.getIdAutor());
                    showInfo("Vínculo removido!");
                    carregarAutoresDoLivro();
                } catch (Exception e) {
                    showError("Erro ao remover: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}