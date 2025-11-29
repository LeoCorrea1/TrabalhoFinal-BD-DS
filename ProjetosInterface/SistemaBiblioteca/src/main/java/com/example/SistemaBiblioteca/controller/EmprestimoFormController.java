package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EmprestimoDAO;
import com.example.SistemaBiblioteca.dao.ExemplarDAO;
import com.example.SistemaBiblioteca.dao.UsuarioDAO;
import com.example.SistemaBiblioteca.model.Emprestimo;
import com.example.SistemaBiblioteca.model.Exemplar;
import com.example.SistemaBiblioteca.model.Usuario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.util.List;

public class EmprestimoFormController {

    @FXML private ComboBox<Exemplar> comboExemplar;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private DatePicker datePrevista;
    @FXML private ChoiceBox<String> choiceStatus;

    private Emprestimo atual;
    private final EmprestimoDAO dao = new EmprestimoDAO();

    @FXML
    public void initialize() {
        // Tipos de status possíveis
        choiceStatus.getItems().addAll("ativo", "devolvido", "atrasado", "perdido");

        // Preenche comboboxes
        ExemplarDAO exemplarDAO = new ExemplarDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Pega todos exemplares do BD e usuários
        List<Exemplar> exemplares = exemplarDAO.findByItem(1); // ajuste caso precise buscar todos
        List<Usuario> usuarios = usuarioDAO.findAll();

        comboExemplar.setItems(FXCollections.observableArrayList(exemplares));
        comboUsuario.setItems(FXCollections.observableArrayList(usuarios));

        // Mostra código de barras + localização
        comboExemplar.setConverter(new StringConverter<>() {
            @Override
            public String toString(Exemplar ex) {
                if (ex == null) return "";
                String loc = ex.getNomeLocalizacao() == null ? "" : " (" + ex.getNomeLocalizacao() + ")";
                return ex.getCodigoBarras() + loc;
            }
            @Override
            public Exemplar fromString(String s) { return null; }
        });

        // Mostra nome do usuário
        comboUsuario.setConverter(new StringConverter<>() {
            @Override
            public String toString(Usuario u) {
                if (u == null) return "";
                return u.getNome() + " (" + u.getTipoUsuario() + ")";
            }
            @Override
            public Usuario fromString(String s) { return null; }
        });
    }

    public void setEmprestimo(Emprestimo e) {
        this.atual = e;
        Platform.runLater(() -> {
            comboExemplar.getItems().stream()
                    .filter(ex -> ex.getIdExemplar().equals(e.getIdExemplar()))
                    .findFirst().ifPresent(comboExemplar::setValue);
            comboUsuario.getItems().stream()
                    .filter(us -> us.getIdUsuario().equals(e.getIdUsuario()))
                    .findFirst().ifPresent(comboUsuario::setValue);
            if (e.getDataPrevistaDevolucao() != null)
                datePrevista.setValue(e.getDataPrevistaDevolucao().toLocalDate());
            choiceStatus.setValue(e.getStatus());
        });
    }

    @FXML
    private void onSalvar() {
        try {
            if (atual == null) atual = new Emprestimo();

            Exemplar ex = comboExemplar.getValue();
            Usuario us = comboUsuario.getValue();

            if (ex == null || us == null) {
                showError("Selecione o exemplar e o usuário.");
                return;
            }

            atual.setIdExemplar(ex.getIdExemplar());
            atual.setIdUsuario(us.getIdUsuario());
            atual.setDataEmprestimo(LocalDateTime.now());
            atual.setDataPrevistaDevolucao(LocalDateTime.of(datePrevista.getValue(), java.time.LocalTime.NOON));
            atual.setStatus(choiceStatus.getValue() == null ? "ativo" : choiceStatus.getValue());

            if (atual.getIdEmprestimo() == null) dao.insert(atual);
            else dao.update(atual);

            showInfo("Empréstimo salvo com sucesso!");
            SceneManager.show("emprestimo_list.fxml", "Empréstimos");

        } catch (Exception ex) {
            showError("Erro ao salvar: " + ex.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        SceneManager.show("emprestimo_list.fxml", "Empréstimos");
    }

    private void showError(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).showAndWait());
    }

    private void showInfo(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, msg).showAndWait());
    }
}