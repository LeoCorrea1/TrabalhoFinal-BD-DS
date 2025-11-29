package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EmprestimoDAO;
import com.example.SistemaBiblioteca.model.Emprestimo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmprestimoListController {

    @FXML private TableView<Emprestimo> table;
    @FXML private TableColumn<Emprestimo,Integer> colId;
    @FXML private TableColumn<Emprestimo,Integer> colExemplar;
    @FXML private TableColumn<Emprestimo,Integer> colUsuario;
    @FXML private TableColumn<Emprestimo,String> colEmprestimo;
    @FXML private TableColumn<Emprestimo,String> colPrevista;
    @FXML private TableColumn<Emprestimo,String> colDevolucao;
    @FXML private TableColumn<Emprestimo,String> colStatus;
    @FXML private Button btnNovo, btnEditar, btnExcluir, btnAtualizar;

    private final EmprestimoDAO dao = new EmprestimoDAO();
    private final ObservableList<Emprestimo> data = FXCollections.observableArrayList();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idEmprestimo"));
        colExemplar.setCellValueFactory(new PropertyValueFactory<>("idExemplar"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colEmprestimo.setCellValueFactory(c-> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        c.getValue().getDataEmprestimo()!=null?fmt.format(c.getValue().getDataEmprestimo()):"")));
        colPrevista.setCellValueFactory(c-> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        c.getValue().getDataPrevistaDevolucao()!=null?fmt.format(c.getValue().getDataPrevistaDevolucao()):"")));
        colDevolucao.setCellValueFactory(c-> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        c.getValue().getDataDevolucao()!=null?fmt.format(c.getValue().getDataDevolucao()):"")));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(data);
        carregar();
    }

    private void carregar(){
        Task<List<Emprestimo>> t = new Task<>() {
            @Override protected List<Emprestimo> call(){ return dao.findAll(); }
        };
        t.setOnSucceeded(e-> data.setAll(t.getValue()));
        new Thread(t).start();
    }

    @FXML private void onAtualizar(){ carregar(); }

    @FXML private void onNovo(){
        SceneManager.show("emprestimo_form.fxml","Novo Empréstimo");
    }

    @FXML private void onEditar(){
        Emprestimo sel = table.getSelectionModel().getSelectedItem();
        if(sel==null){showError("Selecione um empréstimo."); return;}
        SceneManager.show("emprestimo_form.fxml","Editar Empréstimo", l->{
            EmprestimoFormController ctrl = (EmprestimoFormController) l.getController();
            ctrl.setEmprestimo(sel);
        });
    }

    @FXML private void onExcluir(){
        Emprestimo sel = table.getSelectionModel().getSelectedItem();
        if(sel==null){showError("Selecione um registro."); return;}
        if(!confirm("Excluir empréstimo?")) return;
        Task<Void> t = new Task<>() { @Override protected Void call(){ dao.delete(sel.getIdEmprestimo()); return null;} };
        t.setOnSucceeded(e->{ showInfo("Excluído."); carregar(); });
        new Thread(t).start();
    }

    private boolean confirm(String msg){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,msg,ButtonType.YES,ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait();
        return a.getResult()==ButtonType.YES;
    }
    private void showError(String msg){ Platform.runLater(()-> new Alert(Alert.AlertType.ERROR,msg).showAndWait());}
    private void showInfo(String msg){ Platform.runLater(()-> new Alert(Alert.AlertType.INFORMATION,msg).showAndWait());}
}