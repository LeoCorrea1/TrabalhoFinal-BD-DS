package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.EmprestimoDAO;
import com.example.SistemaBiblioteca.model.Emprestimo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

public class EmprestimoFormController {
    @FXML private TextField txtExemplar;
    @FXML private TextField txtUsuario;
    @FXML private DatePicker datePrevista;
    @FXML private ChoiceBox<String> choiceStatus;

    private Emprestimo atual;
    private final EmprestimoDAO dao = new EmprestimoDAO();

    @FXML
    public void initialize(){
        choiceStatus.getItems().addAll("ativo","devolvido","atrasado","perdido");
    }

    public void setEmprestimo(Emprestimo e){
        this.atual = e;
        txtExemplar.setText(String.valueOf(e.getIdExemplar()));
        txtUsuario.setText(String.valueOf(e.getIdUsuario()));
        if(e.getDataPrevistaDevolucao()!=null)
            datePrevista.setValue(e.getDataPrevistaDevolucao().toLocalDate());
        choiceStatus.setValue(e.getStatus());
    }

    @FXML
    private void onSalvar(){
        try{
            if(atual==null) atual = new Emprestimo();
            atual.setIdExemplar(Integer.parseInt(txtExemplar.getText()));
            atual.setIdUsuario(Integer.parseInt(txtUsuario.getText()));
            atual.setDataEmprestimo(LocalDateTime.now());
            atual.setDataPrevistaDevolucao(LocalDateTime.of(datePrevista.getValue(), java.time.LocalTime.NOON));
            atual.setStatus(choiceStatus.getValue()==null? "ativo" : choiceStatus.getValue());
            if(atual.getIdEmprestimo()==null) dao.insert(atual);
            else dao.update(atual);
            showInfo("Salvo.");
            SceneManager.show("emprestimo_list.fxml","Empréstimos");
        }catch(Exception ex){ showError("Erro: "+ex.getMessage());}
    }

    @FXML private void onCancelar(){ SceneManager.show("emprestimo_list.fxml","Empréstimos"); }

    private void showError(String m){ Platform.runLater(()->new Alert(Alert.AlertType.ERROR,m).showAndWait());}
    private void showInfo(String m){ Platform.runLater(()->new Alert(Alert.AlertType.INFORMATION,m).showAndWait());}
}