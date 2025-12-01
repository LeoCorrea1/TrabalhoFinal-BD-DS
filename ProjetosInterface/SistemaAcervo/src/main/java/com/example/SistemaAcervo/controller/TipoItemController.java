package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.TipoItemAcervoDao;
import com.example.SistemaAcervo.model.TipoItemAcervo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Optional;

public class TipoItemController {

    @FXML private TableView<TipoItemAcervo> tableTipos;
    @FXML private TableColumn<TipoItemAcervo, Integer> colId;
    @FXML private TableColumn<TipoItemAcervo, String> colNome;
    @FXML private TableColumn<TipoItemAcervo, String> colDesc;

    private final TipoItemAcervoDao dao = new TipoItemAcervoDao();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getIdTipo()));
        colNome.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getNome()));
        colDesc.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDescricao()));
        refresh();
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    private void refresh() {
        try {
            List<TipoItemAcervo> lista = dao.findAll();
            tableTipos.setItems(FXCollections.observableArrayList(lista));
        } catch (Exception e) {
            showError("Erro ao carregar: " + e.getMessage());
        }
    }

    @FXML
    private void onNovo() {
        Dialog<TipoItemAcervo> dialog = criarDialog(null);
        Optional<TipoItemAcervo> resultado = dialog.showAndWait();

        resultado.ifPresent(tipo -> {
            try {
                dao.insert(tipo);
                showInfo("Tipo criado com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao criar: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEditar() {
        TipoItemAcervo selecionado = tableTipos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showError("Selecione um tipo para editar.");
            return;
        }

        Dialog<TipoItemAcervo> dialog = criarDialog(selecionado);
        Optional<TipoItemAcervo> resultado = dialog.showAndWait();

        resultado.ifPresent(tipo -> {
            try {
                dao.update(tipo);
                showInfo("Tipo atualizado com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao atualizar: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onRemover() {
        TipoItemAcervo selecionado = tableTipos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            showError("Selecione um tipo para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar exclusão");
        confirmacao.setHeaderText("Remover tipo: " + selecionado.getNome() + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                dao.delete(selecionado.getIdTipo());
                showInfo("Tipo removido com sucesso!");
                refresh();
            } catch (Exception e) {
                showError("Erro ao remover: " + e.getMessage());
            }
        }
    }

    private Dialog<TipoItemAcervo> criarDialog(TipoItemAcervo tipoExistente) {
        Dialog<TipoItemAcervo> dialog = new Dialog<>();
        dialog.setTitle(tipoExistente == null ? "Novo Tipo" : "Editar Tipo");
        dialog.setHeaderText(tipoExistente == null ? "Preencha os dados do novo tipo" : "Edite os dados do tipo");

        // Campos
        TextField txtNome = new TextField();
        txtNome.setPromptText("Nome do tipo");

        TextArea txtDescricao = new TextArea();
        txtDescricao.setPromptText("Descrição");
        txtDescricao.setPrefRowCount(3);

        // Preencher se for edição
        if (tipoExistente != null) {
            txtNome.setText(tipoExistente.getNome());
            txtDescricao.setText(tipoExistente.getDescricao());
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(txtNome, 1, 0);
        grid.add(new Label("Descrição:"), 0, 1);
        grid.add(txtDescricao, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Converter resultado
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                TipoItemAcervo tipo = tipoExistente != null ? tipoExistente : new TipoItemAcervo();
                tipo.setNome(txtNome.getText().trim());
                tipo.setDescricao(txtDescricao.getText().trim());
                return tipo;
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