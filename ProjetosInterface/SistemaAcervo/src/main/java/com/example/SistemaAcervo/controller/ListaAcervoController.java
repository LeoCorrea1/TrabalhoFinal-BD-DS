package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.ItemAcervoDao;
import com.example.SistemaAcervo.dao.TipoItemAcervoDao;
import com.example.SistemaAcervo.model.ItemAcervo;
import com.example.SistemaAcervo.model.ItemAcervoCompleto;
import com.example.SistemaAcervo.model.TipoItemAcervo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class ListaAcervoController {

    // Tabela
    @FXML private TableView<ItemAcervo> tableItens;
    @FXML private TableColumn<ItemAcervo, Integer> colId;
    @FXML private TableColumn<ItemAcervo, String> colTitulo;
    @FXML private TableColumn<ItemAcervo, String> colTipo;
    @FXML private TableColumn<ItemAcervo, Integer> colAno;
    @FXML private TextField txtBusca;
    @FXML private ComboBox<TipoItemAcervo> cmbTipo;

    // Painel de detalhes
    @FXML private VBox painelDetalhes;
    @FXML private Label lblSelecioneItem;

    // Informações básicas
    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private Label lblTipo;
    @FXML private Label lblAno;
    @FXML private Label lblIdioma;
    @FXML private Label lblDescricao;

    // Informações do livro
    @FXML private TitledPane paneLivro;
    @FXML private Label lblIsbn;
    @FXML private Label lblEdicao;
    @FXML private Label lblPaginas;
    @FXML private Label lblEditora;

    // Autores
    @FXML private TitledPane paneAutores;
    @FXML private ListView<String> listAutores;
    @FXML private Label lblSemAutores;

    private final ItemAcervoDao itemDao = new ItemAcervoDao();
    private final TipoItemAcervoDao tipoDao = new TipoItemAcervoDao();

    @FXML
    private void initialize() {
        // Configurar colunas da tabela
        colId.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getIdItemAcervo()));
        colTitulo.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTitulo()));
        colTipo.setCellValueFactory(p -> new SimpleStringProperty(getTipoNome(p.getValue().getIdTipo())));
        colAno.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getAno()));

        // Configurar ComboBox de tipos
        cmbTipo.setCellFactory(c -> new ListCell<>() {
            @Override
            protected void updateItem(TipoItemAcervo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        cmbTipo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoItemAcervo item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos" : item.getNome());
            }
        });

        // Listener para seleção na tabela
        tableItens.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarDetalhes(newVal.getIdItemAcervo());
            }
        });

        // Esconder painel de detalhes inicialmente
        esconderDetalhes();

        loadTipos();
        refresh();
    }

    private void loadTipos() {
        try {
            List<TipoItemAcervo> tipos = tipoDao.findAll();
            cmbTipo.setItems(FXCollections.observableArrayList(tipos));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private String getTipoNome(Integer idTipo) {
        if (idTipo == null) return "-";
        try {
            TipoItemAcervo t = tipoDao.findById(idTipo);
            return t != null ? t.getNome() : "-";
        } catch (Exception e) {
            return "-";
        }
    }

    @FXML
    private void onRefresh() {
        refresh();
        esconderDetalhes();
    }

    private void refresh() {
        try {
            List<ItemAcervo> items = itemDao.findAll();
            tableItens.setItems(FXCollections.observableArrayList(items));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onFiltrar() {
        try {
            List<ItemAcervo> items = itemDao.findAll();
            String busca = txtBusca.getText() == null ? "" : txtBusca.getText().trim().toLowerCase();
            TipoItemAcervo tipoSelecionado = cmbTipo.getSelectionModel().getSelectedItem();

            List<ItemAcervo> filtrados = items.stream()
                    .filter(i -> busca.isEmpty() || (i.getTitulo() != null && i.getTitulo().toLowerCase().contains(busca)))
                    .filter(i -> tipoSelecionado == null || (i.getIdTipo() != null && i.getIdTipo().equals(tipoSelecionado.getIdTipo())))
                    .collect(Collectors.toList());

            tableItens.setItems(FXCollections.observableArrayList(filtrados));
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void carregarDetalhes(int idItem) {
        try {
            ItemAcervoCompleto item = itemDao.findByIdCompleto(idItem);

            if (item == null) {
                esconderDetalhes();
                return;
            }

            // Mostrar painel de detalhes
            lblSelecioneItem.setVisible(false);
            lblSelecioneItem.setManaged(false);

            // Preencher informações básicas
            lblTitulo.setText(valorOuVazio(item.getTitulo()));
            lblSubtitulo.setText(valorOuVazio(item.getSubtitulo()));
            lblTipo.setText(valorOuVazio(item.getTipoNome()));
            lblAno.setText(item.getAno() != null ? String.valueOf(item.getAno()) : "-");
            lblIdioma.setText(valorOuVazio(item.getIdioma()));
            lblDescricao.setText(valorOuVazio(item.getDescricao()));

            // Mostrar/esconder seção de livro
            if (item.isEhLivro()) {
                paneLivro.setVisible(true);
                paneLivro.setManaged(true);
                lblIsbn.setText(valorOuVazio(item.getIsbn()));
                lblEdicao.setText(valorOuVazio(item.getEdicao()));
                lblPaginas.setText(item.getNumeroPaginas() != null ? String.valueOf(item.getNumeroPaginas()) : "-");
                lblEditora.setText(valorOuVazio(item.getEditoraNome()));

                // Mostrar autores
                paneAutores.setVisible(true);
                paneAutores.setManaged(true);

                if (item.getAutores().isEmpty()) {
                    listAutores.setVisible(false);
                    listAutores.setManaged(false);
                    lblSemAutores.setVisible(true);
                    lblSemAutores.setManaged(true);
                } else {
                    listAutores.setItems(FXCollections.observableArrayList(item.getAutores()));
                    listAutores.setVisible(true);
                    listAutores.setManaged(true);
                    lblSemAutores.setVisible(false);
                    lblSemAutores.setManaged(false);
                }
            } else {
                paneLivro.setVisible(false);
                paneLivro.setManaged(false);
                paneAutores.setVisible(false);
                paneAutores.setManaged(false);
            }

        } catch (Exception e) {
            showError("Erro ao carregar detalhes: " + e.getMessage());
        }
    }

    private void esconderDetalhes() {
        lblSelecioneItem.setVisible(true);
        lblSelecioneItem.setManaged(true);

        lblTitulo.setText("");
        lblSubtitulo.setText("");
        lblTipo.setText("");
        lblAno.setText("");
        lblIdioma.setText("");
        lblDescricao.setText("");

        paneLivro.setVisible(false);
        paneLivro.setManaged(false);
        paneAutores.setVisible(false);
        paneAutores.setManaged(false);
    }

    private String valorOuVazio(String valor) {
        return (valor == null || valor.isEmpty()) ? "-" : valor;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
}