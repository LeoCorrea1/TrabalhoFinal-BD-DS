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
import java.time.LocalTime;
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

        List<Exemplar> exemplares = new ExemplarDAO().listarTodos();
        List<Usuario> usuarios = new UsuarioDAO().findAll();

        comboExemplar.setItems(FXCollections.observableArrayList(exemplares));
        comboUsuario.setItems(FXCollections.observableArrayList(usuarios));
        comboExemplar.setConverter(new StringConverter<>() {
            @Override
            public String toString(Exemplar e) {
                if (e == null) return "";
                String loc = e.getNomeLocalizacao() == null ? "" : " (" + e.getNomeLocalizacao() + ")";
                return e.getCodigoBarras() + loc;
            }
            @Override
            public Exemplar fromString(String s) { return null; }
        });
        comboUsuario.setConverter(new StringConverter<>() {
            @Override
            public String toString(Usuario u) {
                return u == null ? "" : u.getNome() + " (" + u.getTipoUsuario() + ")";
            }
            @Override
            public Usuario fromString(String s) { return null; }
        });

        choiceStatus.setDisable(true);
    }
    public void setEmprestimo(Emprestimo e) {
        this.atual = e;
        choiceStatus.setDisable(false);

        Platform.runLater(() -> {
            for (Exemplar ex : comboExemplar.getItems()) {
                if (ex.getIdExemplar().equals(e.getIdExemplar())) {
                    comboExemplar.setValue(ex);
                    break;
                }
            }
            for (Usuario us : comboUsuario.getItems()) {
                if (us.getIdUsuario().equals(e.getIdUsuario())) {
                    comboUsuario.setValue(us);
                    break;
                }
            }
            if (e.getDataPrevistaDevolucao() != null) {
                datePrevista.setValue(e.getDataPrevistaDevolucao().toLocalDate());
            }

            choiceStatus.setValue(e.getStatus());
        });
    }

    @FXML
    private void onSalvar() {
        try {
            Exemplar ex = comboExemplar.getValue();
            Usuario us = comboUsuario.getValue();

            if (ex == null || us == null) {
                showError("Selecione exemplar e usuário.");
                return;
            }
            if (datePrevista.getValue() == null) {
                showError("Selecione a data prevista de devolução.");
                return;
            }
            boolean isNovo = (atual == null || atual.getIdEmprestimo() == null);

            if (isNovo) {
                if (new ReservaDAO().existeReservaAtivaOuValida(ex.getIdExemplar())) {
                    showError("Este exemplar possui uma reserva ativa.");
                    return;
                }
                atual = new Emprestimo();
            }
            atual.setIdExemplar(ex.getIdExemplar());
            atual.setIdUsuario(us.getIdUsuario());
            atual.setDataPrevistaDevolucao(LocalDateTime.of(datePrevista.getValue(), LocalTime.NOON));
            MovimentacaoDAO movDAO = new MovimentacaoDAO();
            if (isNovo) {
                atual.setStatus("ativo");
                atual.setDataEmprestimo(LocalDateTime.now());
                dao.insert(atual);
                movDAO.registrar(null, ex.getIdExemplar(), us.getIdUsuario(),
                        "emprestimo", "Empréstimo criado para " + us.getNome());
            } else {
                atual.setStatus(choiceStatus.getValue());

                if ("devolvido".equalsIgnoreCase(atual.getStatus())) {
                    atual.setDataDevolucao(LocalDateTime.now());
                    movDAO.registrar(null, ex.getIdExemplar(), us.getIdUsuario(),
                            "devolucao", "Devolução por " + us.getNome());
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
    @FXML
    public void onVoltar() {
        SceneManager.show("emprestimo_list.fxml", "Empréstimos");
    }
    private void showError(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).showAndWait());
    }
    private void showInfo(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, msg).showAndWait());
    }
}