package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.AutorDao;
import com.example.SistemaAcervo.model.Autor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Optional;

public class AutorController {

    @FXML private TableView<Autor> tableAutores;
    @FXML private TableColumn<Autor, Integer> colId;
    @FXML private TableColumn<Autor, String> colNome;
    @FXML private TableColumn<Autor, String> colSobrenome;
    @FXML private TableColumn<Autor, String> colNacionalidade;

    private final AutorDao dao = new AutorDao();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getIdAutor()));
        colNome.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNome()));
        colSobrenome.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getSobrenome()));
        colNacionalidade.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNacionalidade()));
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        try {
            List<Autor> lista = dao.findAll();
            tableAutores.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            showError("Erro ao carregar: " + e.getMessage());
        }
    }

    @FXML
    private void onNovo() {
        Dialog<Autor> dialog = criarDialog(null);
        Optional<Autor> resultado = dialog.showAndWait();

        resultado.ifPresent(autor -> {
            try {
                dao.insert(autor);
                showInfo("Autor criado com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao criar: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEditar() {
        Autor selecionado = tableAutores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showError("Selecione um autor para editar.");
            return;
        }

        Dialog<Autor> dialog = criarDialog(selecionado);
        Optional<Autor> resultado = dialog.showAndWait();

        resultado.ifPresent(autor -> {
            try {
                dao.update(autor);
                showInfo("Autor atualizado com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao atualizar: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onRemover() {
        Autor selecionado = tableAutores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showError("Selecione um autor para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Remover autor: " + selecionado.getNome() + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                dao.delete(selecionado.getIdAutor());
                showInfo("Autor removido com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao remover: " + e.getMessage());
            }
        }
    }

    private Dialog<Autor> criarDialog(Autor autorExistente) {
        Dialog<Autor> dialog = new Dialog<>();
        dialog.setTitle(autorExistente == null ? "Novo Autor" : "Editar Autor");
        dialog.setHeaderText(autorExistente == null ? "Preencha os dados" : "Edite os dados");

        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome");

        TextField txtSobrenome = new TextField();
        txtSobrenome.setPromptText("Sobrenome");

        TextField txtNacionalidade = new TextField();
        txtNacionalidade.setPromptText("Nacionalidade");

        if (autorExistente != null) {
            txtNome.setText(autorExistente.getNome());
            txtSobrenome.setText(autorExistente.getSobrenome());
            txtNacionalidade.setText(autorExistente.getNacionalidade());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Sobrenome:"), 0, 1);
        grid.add(txtSobrenome, 1, 1);
        grid.add(new Label("Nacionalidade:"), 0, 2);
        grid.add(txtNacionalidade, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Autor a = autorExistente != null ? autorExistente : new Autor();
                a.setNome(txtNome.getText().trim());
                a.setSobrenome(txtSobrenome.getText().trim());
                a.setNacionalidade(txtNacionalidade.getText().trim());
                return a;
            }
            return null;
        });

        return dialog;
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