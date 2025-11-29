package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.ItemAcervoDAO;
import com.example.SistemaBiblioteca.dao.LivroDAO;
import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.model.Livro;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;

public class ItemAcervoController {

    @FXML private TableView<ItemAcervo> tableView;
    @FXML private TableColumn<ItemAcervo, Integer> colId;
    @FXML private TableColumn<ItemAcervo, String> colTitulo;
    @FXML private TableColumn<ItemAcervo, Integer> colAno;
    @FXML private TableColumn<ItemAcervo, String> colIdioma;
    @FXML private TableColumn<ItemAcervo, String> colTipo;
    @FXML private TextField searchField;

    // Botões opcionais — se existirem no FXML, referencie-os; se não, null checks evitam NPE.
    @FXML private Button btnEditar;
    @FXML private Button btnExcluir;
    @FXML private Button btnNovo;
    @FXML private Button btnRefresh;
    @FXML private Button btnExemplares;


    private final ItemAcervoDAO dao = new ItemAcervoDAO();
    private final ObservableList<ItemAcervo> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("initialize ItemAcervoController");

        // configurar colunas (usa PropertyValueFactory -> reflection; module deve abrir pacote model)
        colId.setCellValueFactory(new PropertyValueFactory<>("idItemAcervo"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idioma"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoNome"));

        tableView.setItems(data);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean sel = newSel != null;
            if (btnEditar != null) btnEditar.setDisable(!sel);
            if (btnExcluir != null) btnExcluir.setDisable(!sel);
            if (btnExemplares != null) btnExemplares.setDisable(!sel);
        });
        if (btnExemplares != null) btnExemplares.setDisable(true);



        // carregar dados inicialmente
        carregarDados();
    }
    @FXML public void onVoltar() { SceneManager.show("dashboard.fxml","Painel"); }

    private void carregarDados() {
        Task<List<ItemAcervo>> task = new Task<>() {
            @Override
            protected List<ItemAcervo> call() throws Exception {
                System.out.println("DEBUG: chamando dao.findAll()");
                List<ItemAcervo> list = dao.findAll();
                System.out.println("DEBUG: dao.findAll() retornou size=" + (list == null ? "null" : list.size()));
                return list;
            }
        };

        task.setOnSucceeded(evt -> {
            List<ItemAcervo> value = task.getValue();
            data.setAll(value == null ? List.of() : value);
            System.out.println("DEBUG: tabela atualizada com " + data.size() + " itens");
        });

        task.setOnFailed(evt -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showError("Erro ao carregar itens: " + ex.getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    public void onPesquisar() {
        String q = searchField.getText();
        if (q == null || q.isBlank()) {
            carregarDados();
            return;
        }

        Task<List<ItemAcervo>> task = new Task<>() {
            @Override
            protected List<ItemAcervo> call() throws Exception {
                List<ItemAcervo> all = dao.findAll();
                return all.stream()
                        .filter(i -> i.getTitulo() != null && i.getTitulo().toLowerCase().contains(q.toLowerCase()))
                        .toList();
            }
        };

        task.setOnSucceeded(e -> data.setAll(task.getValue()));
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showError("Erro na pesquisa: " + ex.getMessage());
        });
        new Thread(task).start();
    }

    @FXML
    public void onNovo() {
        try {
            // abre formulário de criação (usa SceneManager.show para comportamento consistente)
            SceneManager.show("livro_form.fxml", "Novo Item");
        } catch (Exception ex) {
            // fallback: tentar carregar diretamente
            ex.printStackTrace();
            showError("Erro ao abrir formulário: " + ex.getMessage());
        }
    }

    @FXML
    public void onRefresh() {
        carregarDados();
    }

    @FXML
    public void onEditar() {
        ItemAcervo sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Selecione um item para editar.");
            return;
        }

        // busca o Livro correspondente (pode não existir — ex.: item que não é livro)
        Task<Livro> task = new Task<>() {
            @Override
            protected Livro call() throws Exception {
                LivroDAO livroDAO = new LivroDAO();
                return livroDAO.findById(sel.getIdItemAcervo());
            }
        };

        task.setOnSucceeded(evt -> {
            Livro livro = task.getValue(); // pode ser null
            // abrir o FXML e passar os objetos para edição (na UI thread)
            Platform.runLater(() -> {
                try {
                    URL url = SceneManager.class.getResource("/fxml/livro_form.fxml");
                    if (url == null) throw new RuntimeException("FXML livro_form.fxml não encontrado em /fxml/");

                    FXMLLoader loader = new FXMLLoader(url);
                    Parent root = loader.load();

                    // obter controller e injetar dados
                    Object ctrlObj = loader.getController();
                    if (ctrlObj instanceof com.example.SistemaBiblioteca.controller.LivroFormController) {
                        com.example.SistemaBiblioteca.controller.LivroFormController ctrl =
                                (com.example.SistemaBiblioteca.controller.LivroFormController) ctrlObj;
                        ctrl.setEditing(sel, livro);
                    }

                    Scene scene = new Scene(root);

                    // obtém stage atual a partir de qualquer nó (tableView)
                    Stage stage = (Stage) tableView.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Editar Item");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Erro ao abrir editor: " + e.getMessage());
                }
            });
        });

        task.setOnFailed(evt -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showError("Erro ao carregar dados do livro: " + ex.getMessage());
        });

        new Thread(task).start();
    }

    @FXML
    public void onExcluir() {
        ItemAcervo sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Selecione um item para excluir.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirma exclusão do item \"" + sel.getTitulo() + "\" ?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        ItemAcervoDAO daoLocal = new ItemAcervoDAO();
                        boolean ok = daoLocal.deleteIfNoDependencies(sel.getIdItemAcervo());
                        if (!ok) throw new RuntimeException("Item possui dependências. Remova dependências antes de excluir.");
                        return null;
                    }
                };

                task.setOnSucceeded(evt -> {
                    showInfo("Item excluído com sucesso.");
                    carregarDados();
                });

                task.setOnFailed(evt -> {
                    Throwable ex = task.getException();
                    ex.printStackTrace();
                    showError("Erro ao excluir item: " + ex.getMessage());
                });

                new Thread(task).start();
            }
        });
    }

    // helpers (UI)
    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            a.setHeaderText("Erro");
            a.showAndWait();
        });
    }

    private void showInfo(String msg) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
            a.setHeaderText(null);
            a.showAndWait();
        });
    }

    // dentro de ItemAcervoController.java
    @FXML
    public void onOpenEditoras() {
        // simples: abre a tela de editoras. Ajuste o nome do FXML se o seu for diferente.
        SceneManager.show("editoras_list.fxml", "Editoras");
    }
    @FXML
    public void onExemplares() {
        ItemAcervo item = tableView.getSelectionModel().getSelectedItem();
        if (item == null) return;

        SceneManager.show("exemplar_list.fxml", "Exemplares", loader -> {
            ExemplarListController ctrl = loader.getController();
            ctrl.setItemAcervoId(item.getIdItemAcervo());
        });
    }
}