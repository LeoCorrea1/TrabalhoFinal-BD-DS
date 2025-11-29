package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.*;
import com.example.SistemaBiblioteca.model.*;
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
        choiceStatus.getItems().addAll("ativo", "devolvido", "atrasado", "perdido");

        ExemplarDAO exemplarDAO = new ExemplarDAO();
        UsuarioDAO usuarioDAO  = new UsuarioDAO();

        List<Exemplar> exemplares = exemplarDAO.listarTodos();
        List<Usuario>  usuarios   = usuarioDAO.findAll();

        comboExemplar.setItems(FXCollections.observableArrayList(exemplares));
        comboUsuario.setItems(FXCollections.observableArrayList(usuarios));

        comboExemplar.setConverter(new StringConverter<>() {
            @Override public String toString(Exemplar ex) {
                if (ex == null) return "";
                String loc = ex.getNomeLocalizacao() == null ? "" : " (" + ex.getNomeLocalizacao() + ")";
                return ex.getCodigoBarras() + loc;
            }
            @Override public Exemplar fromString(String s) { return null; }
        });
        comboUsuario.setConverter(new StringConverter<>() {
            @Override public String toString(Usuario u) {
                return (u == null ? "" : u.getNome() + " (" + u.getTipoUsuario() + ")");
            }
            @Override public Usuario fromString(String s) { return null; }
        });

        choiceStatus.setDisable(true);
    }

    public void setEmprestimo(Emprestimo e) {
        this.atual = e;
        choiceStatus.setDisable(false);
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
            boolean novo = (atual == null || atual.getIdEmprestimo() == null);
            if (atual == null) atual = new Emprestimo();

            Exemplar ex = comboExemplar.getValue();
            Usuario us  = comboUsuario.getValue();

            if (ex == null || us == null) {
                showError("Selecione o exemplar e o usuário.");
                return;
            }

            // 🔒 validação de reserva ativa ou válida
            ReservaDAO reservaDAO = new ReservaDAO();
            if (reservaDAO.existeReservaAtivaOuValida(ex.getIdExemplar())) {
                showError("Este exemplar possui uma reserva ativa e não pode ser emprestado.");
                return;
            }

            atual.setIdExemplar(ex.getIdExemplar());
            atual.setIdUsuario(us.getIdUsuario());

            if (novo) {
                atual.setStatus("ativo");
                atual.setDataEmprestimo(LocalDateTime.now());
                atual.setDataPrevistaDevolucao(LocalDateTime.of(
                        datePrevista.getValue(), java.time.LocalTime.NOON));
                dao.insert(atual);
            } else {
                atual.setStatus(choiceStatus.getValue());
                atual.setDataPrevistaDevolucao(LocalDateTime.of(
                        datePrevista.getValue(), java.time.LocalTime.NOON));
                if ("devolvido".equalsIgnoreCase(atual.getStatus())) {
                    atual.setDataDevolucao(LocalDateTime.now());
                }
                dao.update(atual);
            }

            showInfo("Empréstimo salvo com sucesso!");
            SceneManager.show("emprestimo_list.fxml", "Empréstimos");

        } catch (Exception ex) {
            showError("Erro ao salvar: " + ex.getMessage());
            ex.printStackTrace();
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