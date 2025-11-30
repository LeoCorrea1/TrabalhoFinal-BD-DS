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

public class ReservaFormController {

    @FXML private ComboBox<Exemplar> comboExemplar;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private DatePicker dateExpiracao;
    @FXML private ChoiceBox<String> choiceStatus;

    private Reserva atual;
    private final ReservaDAO dao = new ReservaDAO();

    @FXML
    public void initialize() {
        choiceStatus.getItems().addAll("ativa", "cancelada", "expirada", "convertida");

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
                return u == null ? "" : u.getNome();
            }
            @Override
            public Usuario fromString(String s) { return null; }
        });

        choiceStatus.setDisable(true);
    }

    public void setReserva(Reserva r) {
        this.atual = r;
        choiceStatus.setDisable(false);

        Platform.runLater(() -> {
            // Selecionar o exemplar correto
            for (Exemplar ex : comboExemplar.getItems()) {
                if (ex.getIdExemplar().equals(r.getIdExemplar())) {
                    comboExemplar.setValue(ex);
                    break;
                }
            }

            // Selecionar o usuário correto
            for (Usuario us : comboUsuario.getItems()) {
                if (us.getIdUsuario().equals(r.getIdUsuario())) {
                    comboUsuario.setValue(us);
                    break;
                }
            }

            // Preencher data de expiração
            if (r.getDataExpiracao() != null) {
                dateExpiracao.setValue(r.getDataExpiracao().toLocalDate());
            }

            // Preencher status
            choiceStatus.setValue(r.getStatus());
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

            boolean isNovo = (atual == null || atual.getIdReserva() == null);

            if (isNovo) {
                atual = new Reserva();
            }

            atual.setIdExemplar(ex.getIdExemplar());
            atual.setIdUsuario(us.getIdUsuario());

            MovimentacaoDAO movDAO = new MovimentacaoDAO();

            if (isNovo) {
                atual.setDataReserva(LocalDateTime.now());
                atual.setDataExpiracao(LocalDateTime.now().plusHours(24));
                atual.setStatus("ativa");
                dao.insert(atual);
                movDAO.registrar(null, ex.getIdExemplar(), us.getIdUsuario(),
                        "reserva", "Reserva criada para " + us.getNome());
            } else {
                atual.setStatus(choiceStatus.getValue());
                if (dateExpiracao.getValue() != null) {
                    atual.setDataExpiracao(dateExpiracao.getValue().atStartOfDay());
                }
                dao.update(atual);
            }

            showInfo("Reserva salva com sucesso!");
            SceneManager.show("reserva_list.fxml", "Reservas");

        } catch (Exception e) {
            showError("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onVoltar() {
        SceneManager.show("reserva_list.fxml", "Reservas");
    }

    @FXML
    private void onCancelar() {
        SceneManager.show("reserva_list.fxml", "Reservas");
    }

    private void showError(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).showAndWait());
    }

    private void showInfo(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION, msg).showAndWait());
    }
}