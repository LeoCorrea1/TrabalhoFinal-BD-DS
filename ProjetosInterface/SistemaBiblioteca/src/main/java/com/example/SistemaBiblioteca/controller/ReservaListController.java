package com.example.SistemaBiblioteca.controller;

import com.example.SistemaBiblioteca.app.SceneManager;
import com.example.SistemaBiblioteca.dao.*;
import com.example.SistemaBiblioteca.model.Reserva;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservaListController {

    @FXML private TableView<Reserva> table;
    @FXML private TableColumn<Reserva,Integer> colId;
    @FXML private TableColumn<Reserva,String>  colExemplar;
    @FXML private TableColumn<Reserva,String>  colUsuario;
    @FXML private TableColumn<Reserva,String>  colDataReserva;
    @FXML private TableColumn<Reserva,String>  colExpiracao;
    @FXML private TableColumn<Reserva,String>  colStatus;

    private final ReservaDAO dao=new ReservaDAO();
    private final ObservableList<Reserva> data=FXCollections.observableArrayList();
    private final DateTimeFormatter fmt=DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        colExemplar.setCellValueFactory(new PropertyValueFactory<>("codigoExemplar"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nomeUsuario"));
        colDataReserva.setCellValueFactory(c->new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataReserva()==null?"":fmt.format(c.getValue().getDataReserva())));
        colExpiracao.setCellValueFactory(c->new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataExpiracao()==null?"":fmt.format(c.getValue().getDataExpiracao())));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        table.setItems(data);
        carregar();
    }

    private void carregar(){
        Task<List<Reserva>>t=new Task<>() {
            @Override protected List<Reserva> call(){
                dao.expirarReservasVencidas();return dao.findAllComNomes();
            }};
        t.setOnSucceeded(e->data.setAll(t.getValue()));
        new Thread(t).start();
    }

    @FXML private void onAtualizar(){carregar();}
    @FXML private void onNovo(){SceneManager.show("reserva_form.fxml","Nova Reserva");}

    @FXML private void onEditar(){
        Reserva sel=table.getSelectionModel().getSelectedItem();
        if(sel==null){showError("Selecione uma reserva.");return;}
        SceneManager.show("reserva_form.fxml","Editar Reserva",loader->{
            ReservaFormController ctrl=loader.getController();
            ctrl.setReserva(sel);
        });
    }

    @FXML private void onExcluir(){
        Reserva sel=table.getSelectionModel().getSelectedItem();
        if(sel==null){showError("Selecione uma reserva.");return;}
        Alert a=new Alert(Alert.AlertType.CONFIRMATION,"Excluir reserva?",ButtonType.YES,ButtonType.NO);
        a.showAndWait();
        if(a.getResult()==ButtonType.YES){
            dao.delete(sel.getIdReserva(),sel.getIdExemplar());
            carregar();
        }
    }

    private void showError(String msg){Platform.runLater(()->new Alert(Alert.AlertType.ERROR,msg).showAndWait());}
}