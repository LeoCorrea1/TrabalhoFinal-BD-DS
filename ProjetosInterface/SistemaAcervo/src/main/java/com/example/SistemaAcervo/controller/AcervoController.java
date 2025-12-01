package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.AcervoDAO;
import com.example.SistemaAcervo.model.Acervo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AcervoController {

    @FXML private TextField txtNome;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private DatePicker dpData;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtResponsavel;
    @FXML private TextField txtArquivo;
    @FXML private TextField txtBuscar;
    @FXML private TableView<Acervo> tabela;
    @FXML private TableColumn<Acervo, Integer> colId;
    @FXML private TableColumn<Acervo, String> colNome;
    @FXML private TableColumn<Acervo, String> colCategoria;
    @FXML private TableColumn<Acervo, String> colTipo;

    private final AcervoDAO dao = new AcervoDAO();
    private final ObservableList<Acervo> dados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // preencher combo boxes (ajuste conforme necessidade)
        cbCategoria.getItems().addAll("História","Arte","Cultura Afro","Folclore","Documentos");
        cbTipo.getItems().addAll("Documento","Foto","Obra física","Áudio","Vídeo");

        // configurar colunas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        carregarTabela();

        // selecionar item para preencher formulário ao clicar
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) preencherForm(newV);
        });
    }

    private void carregarTabela() {
        try {
            List<Acervo> lista = dao.listar();
            dados.setAll(lista);
            tabela.setItems(dados);
        } catch (SQLException e) {
            showError("Erro ao listar acervo: " + e.getMessage());
        }
    }

    @FXML
    private void onSalvar() {
        try {
            Acervo a = readForm();
            dao.inserir(a);
            limparForm();
            carregarTabela();
            showInfo("Inserido com sucesso");
        } catch (SQLException e) {
            showError("Erro ao inserir: " + e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        Acervo sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Selecione um item para editar"); return; }
        try {
            Acervo a = readForm();
            a.setId(sel.getId());
            dao.atualizar(a);
            limparForm();
            carregarTabela();
            showInfo("Atualizado com sucesso");
        } catch (SQLException e) {
            showError("Erro ao atualizar: " + e.getMessage());
        }
    }

    @FXML
    private void onExcluir() {
        Acervo sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { showError("Selecione um item para excluir"); return; }
        Alert confirma = new Alert(Alert.AlertType.CONFIRMATION, "Excluir item selecionado?", ButtonType.YES, ButtonType.NO);
        confirma.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try {
                    dao.deletar(sel.getId());
                    limparForm();
                    carregarTabela();
                    showInfo("Excluído com sucesso");
                } catch (SQLException e) {
                    showError("Erro ao excluir: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onUpload() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Escolher arquivo");
        File f = fc.showOpenDialog(null);
        if (f != null) {
            // Aqui estou apenas guardando o caminho do arquivo local.
            // Em produção, copie o arquivo para uma pasta controlada e salve o novo path no BD.
            txtArquivo.setText(f.getAbsolutePath());
        }
    }

    @FXML
    private void onBuscar() {
        String termo = txtBuscar.getText();
        try {
            if (termo == null || termo.isBlank()) carregarTabela();
            else {
                List<Acervo> list = dao.buscar(termo);
                dados.setAll(list);
                tabela.setItems(dados);
            }
        } catch (SQLException e) {
            showError("Erro na busca: " + e.getMessage());
        }
    }

    private Acervo readForm() {
        Acervo a = new Acervo();
        a.setNome(txtNome.getText());
        a.setDescricao(txtDescricao.getText());
        a.setCategoria(cbCategoria.getValue());
        LocalDate d = dpData.getValue();
        a.setDataItem(d);
        a.setTipo(cbTipo.getValue());
        a.setResponsavel(txtResponsavel.getText());
        a.setArquivoPath(txtArquivo.getText());
        a.setStatus("ativo");
        return a;
    }

    private void preencherForm(Acervo a) {
        txtNome.setText(a.getNome());
        txtDescricao.setText(a.getDescricao());
        cbCategoria.setValue(a.getCategoria());
        dpData.setValue(a.getDataItem());
        cbTipo.setValue(a.getTipo());
        txtResponsavel.setText(a.getResponsavel());
        txtArquivo.setText(a.getArquivoPath());
    }

    @FXML
    private void limparForm() {
        txtNome.clear();
        txtDescricao.clear();
        cbCategoria.getSelectionModel().clearSelection();
        dpData.setValue(null);
        cbTipo.getSelectionModel().clearSelection();
        txtResponsavel.clear();
        txtArquivo.clear();
        tabela.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }
}
