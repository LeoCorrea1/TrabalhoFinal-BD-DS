// java
package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EditoraDAO;
import com.example.SistemaBiblioteca.dao.ItemAcervoDAO;
import com.example.SistemaBiblioteca.dao.TipoItemAcervoDAO;
import com.example.SistemaBiblioteca.model.Editora;
import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.model.Livro;
import com.example.SistemaBiblioteca.model.TipoItemAcervo;
import com.example.SistemaBiblioteca.service.LivroService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller do formulário de Livro.
 * - suporta setEditing(itemAcervo, livro) para edição
 * - cria/atualiza via LivroService
 * - carrega ComboBox de Tipo e Editora
 */
public class LivroFormController {

    @FXML private ComboBox<TipoItemAcervo> comboTipo;
    @FXML private ComboBox<Editora> comboEditora;
    @FXML private TextField tituloField;
    @FXML private TextField subtituloField;
    @FXML private TextField anoField;
    @FXML private TextField idiomaField;
    @FXML private TextArea descricaoField;
    @FXML private TextField isbnField;
    @FXML private TextField edicaoField;
    @FXML private TextField paginasField;

    private final TipoItemAcervoDAO tipoDAO = new TipoItemAcervoDAO();
    private final EditoraDAO editoraDAO = new EditoraDAO();
    private final ItemAcervoDAO itemDAO = new ItemAcervoDAO();
    private final LivroService livroService = new LivroService();

    // estado de edição
    private ItemAcervo editingItem = null;
    private Livro editingLivro = null;

    @FXML
    public void initialize() {
        // carregar tipos e editoras em background
        carregarTipos();
        carregarEditoras();
    }

    private void carregarTipos() {
        Task<List<TipoItemAcervo>> task = new Task<>() {
            @Override
            protected List<TipoItemAcervo> call() throws Exception {
                return tipoDAO.findAll();
            }
        };
        task.setOnSucceeded(e -> {
            List<TipoItemAcervo> tipos = task.getValue();
            comboTipo.setItems(FXCollections.observableArrayList(tipos));
            // tenta selecionar "Livro" automaticamente (se existir)
            tipos.stream()
                    .filter(t -> t.getNome() != null && t.getNome().equalsIgnoreCase("livro"))
                    .findFirst().ifPresent(comboTipo::setValue);
        });
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }
    @FXML
    public void onVoltar() {
        SceneManager.show("itemacervo_list.fxml", "Itens do Acervo");
    }

    private void carregarEditoras() {
        Task<List<Editora>> task = new Task<>() {
            @Override
            protected List<Editora> call() throws Exception {
                return editoraDAO.findAll();
            }
        };
        task.setOnSucceeded(e -> {
            comboEditora.setItems(FXCollections.observableArrayList(task.getValue()));
        });
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }

    public void setEditing(ItemAcervo item, Livro livro) {
        this.editingItem = item;
        this.editingLivro = livro;

        Platform.runLater(() -> {
            if (item != null) {
                tituloField.setText(nullSafe(item.getTitulo()));
                subtituloField.setText(nullSafe(item.getSubtitulo()));
                // item.getAno() é int primitivo — usamos 0 como "ausente"
                anoField.setText(item.getAno() == 0 ? "" : String.valueOf(item.getAno()));
                idiomaField.setText(nullSafe(item.getIdioma()));
                descricaoField.setText(nullSafe(item.getDescricao()));

                // seleciona tipo se já carregado (idTipo é int primitivo — 0 = ausente)
                if (item.getIdTipo() != 0 && comboTipo.getItems() != null) {
                    comboTipo.getItems().stream()
                            .filter(t -> Objects.equals(t.getIdTipo(), item.getIdTipo()))
                            .findFirst().ifPresent(comboTipo::setValue);
                }
            }

            if (livro != null) {
                isbnField.setText(nullSafe(livro.getIsbn()));
                edicaoField.setText(nullSafe(livro.getEdicao()));
                paginasField.setText(livro.getNumeroPaginas() == null ? "" : String.valueOf(livro.getNumeroPaginas()));

                if (livro.getIdEditora() != null && comboEditora.getItems() != null) {
                    // aqui usamos getId() do model Editora (ajustado ao model que você tem)
                    comboEditora.getItems().stream()
                            .filter(ed -> Objects.equals(ed.getId(), livro.getIdEditora()))
                            .findFirst().ifPresent(comboEditora::setValue);
                }
            }
        });
    }

    @FXML
    public void onSalvar() {
        // validações mínimas
        String titulo = tituloField.getText();
        if (titulo == null || titulo.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Título obrigatório");
            return;
        }

        TipoItemAcervo tipoSel = comboTipo.getValue();
        if (tipoSel == null) {
            showAlert(Alert.AlertType.ERROR, "Selecione o tipo (ex: Livro)");
            return;
        }

        Editora editoraSel = comboEditora.getValue();

        // montar objetos
        ItemAcervo item = (editingItem != null) ? editingItem : new ItemAcervo();
        item.setTitulo(titulo.trim());
        item.setSubtitulo(emptyToNull(subtituloField.getText()));
        item.setIdioma(emptyToNull(idiomaField.getText()));
        item.setDescricao(emptyToNull(descricaoField.getText()));
        Integer anoParsed = parseIntegerOrNull(anoField.getText());
        item.setAno(anoParsed != null ? anoParsed : 0); // evita autounboxing de null
        item.setIdTipo(tipoSel.getIdTipo());

        Livro livro = (editingLivro != null) ? editingLivro : new Livro();
        livro.setIsbn(emptyToNull(isbnField.getText()));
        livro.setEdicao(emptyToNull(edicaoField.getText()));
        Integer paginasParsed = parseIntegerOrNull(paginasField.getText());
        livro.setNumeroPaginas(paginasParsed != null ? paginasParsed : 0);
        livro.setIdEditora(editoraSel == null ? null : editoraSel.getId()); // usa getId()

        // operação em background (criar ou atualizar)
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (editingLivro == null) {
                    // criação: LivroService cria ItemAcervo + Livro em transação
                    livroService.criarLivroComItem(item, livro);
                } else {
                    // atualização: atualiza item e livro
                    livroService.atualizarLivroComItem(item, livro);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Salvo com sucesso");
            SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            String msg = ex == null ? "Erro desconhecido" : ex.getMessage();
            showAlert(Alert.AlertType.ERROR, "Erro ao salvar: " + msg);
            ex.printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    public void onExcluir() {
        if (editingItem == null) {
            showAlert(Alert.AlertType.ERROR, "Só é possível excluir um item já existente.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirma exclusão do item \"" + editingItem.getTitulo() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        boolean ok = itemDAO.deleteIfNoDependencies(editingItem.getIdItemAcervo());
                        if (!ok) throw new IllegalStateException("Item possui dependências — não pode ser excluído.");
                        return null;
                    }
                };

                task.setOnSucceeded(ev -> {
                    showAlert(Alert.AlertType.INFORMATION, "Item excluído com sucesso.");
                    SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
                });

                task.setOnFailed(ev -> {
                    Throwable ex = task.getException();
                    showAlert(Alert.AlertType.ERROR, "Erro ao excluir: " + (ex == null ? "Erro desconhecido" : ex.getMessage()));
                    if (ex != null) ex.printStackTrace();
                });

                new Thread(task).start();
            }
        });
    }

    @FXML
    public void onCancelar() {
        SceneManager.show("itemacervo_list.fxml", "Gerenciar Acervo");
    }

    // --- utilidades ---
    private Integer parseIntegerOrNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    // adiciona nullSafe que retorna "" quando for null (usado para preencher campos)
    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(type, msg, ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
        });
    }
}
