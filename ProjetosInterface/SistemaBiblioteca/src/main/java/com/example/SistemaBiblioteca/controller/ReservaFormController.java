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
        choiceStatus.getItems().addAll("ativa","cancelada","expirada","convertida");

        ExemplarDAO exDao = new ExemplarDAO();
        UsuarioDAO usDao = new UsuarioDAO();
        List<Exemplar> exemplares = exDao.listarTodos();
        List<Usuario> usuarios = usDao.findAll();

        comboExemplar.setItems(FXCollections.observableArrayList(exemplares));
        comboUsuario.setItems(FXCollections.observableArrayList(usuarios));

        comboExemplar.setConverter(new StringConverter<>() {
            @Override public String toString(Exemplar e) {
                if (e == null) return "";
                String loc = e.getNomeLocalizacao() == null ? "" : " (" + e.getNomeLocalizacao() + ")";
                return e.getCodigoBarras() + loc;
            }
            @Override public Exemplar fromString(String s) { return null; }
        });

        comboUsuario.setConverter(new StringConverter<>() {
            @Override public String toString(Usuario u) {
                return (u == null ? "" : u.getNome());
            }
            @Override public Usuario fromString(String s) { return null; }
        });
    }

    public void setReserva(Reserva r) {
        this.atual = r;
        Platform.runLater(() -> {
            comboExemplar.getItems().stream()
                    .filter(ex -> ex.getIdExemplar().equals(r.getIdExemplar()))
                    .findFirst().ifPresent(comboExemplar::setValue);
            comboUsuario.getItems().stream()
                    .filter(us -> us.getIdUsuario().equals(r.getIdUsuario()))
                    .findFirst().ifPresent(comboUsuario::setValue);
            dateExpiracao.setValue(r.getDataExpiracao().toLocalDate());
            choiceStatus.setValue(r.getStatus());
        });
    }

    @FXML
    private void onSalvar() {
        try {
            boolean novo = (atual == null || atual.getIdReserva() == null);
            if (atual == null) atual = new Reserva();

            Exemplar ex = comboExemplar.getValue();
            Usuario us = comboUsuario.getValue();
            if (ex == null || us == null) {
                showError("Selecione exemplar e usuário.");
                return;
            }

            atual.setIdExemplar(ex.getIdExemplar());
            atual.setIdUsuario(us.getIdUsuario());

            if (novo) {
                atual.setDataReserva(LocalDateTime.now());
                atual.setDataExpiracao(LocalDateTime.now().plusHours(24));
                atual.setStatus("ativa");
                dao.insert(atual);
            } else {
                atual.setStatus(choiceStatus.getValue());
                dao.update(atual);
            }

            showInfo("Reserva salva com sucesso!");
            SceneManager.show("reserva_list.fxml", "Reservas");

        } catch (Exception e) {
            showError("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML private void onCancelar() {
        SceneManager.show("reserva_list.fxml", "Reservas");
    }

    private void showError(String msg){ Platform.runLater(() -> new Alert(Alert.AlertType.ERROR,msg).showAndWait()); }
    private void showInfo(String msg){ Platform.runLater(() -> new Alert(Alert.AlertType.INFORMATION,msg).showAndWait()); }
}