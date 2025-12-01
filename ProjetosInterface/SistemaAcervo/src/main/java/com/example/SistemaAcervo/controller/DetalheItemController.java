package com.example.SistemaAcervo.controller;

import com.example.SistemaAcervo.dao.*;
import com.example.SistemaAcervo.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.List;
import java.util.Optional;

public class DetalheItemController {
    @FXML private Label lblTitulo;
    @FXML private Label lblSub;
    @FXML private Label lblAno;
    @FXML private Label lblIdioma;
    @FXML private ComboBox<TipoItemAcervo> cmbTipo;
    @FXML private TextArea txtDesc;
    @FXML private ListView<Autor> listAutores;

    private final ItemAcervoDao itemDao = new ItemAcervoDao();
    private final TipoItemAcervoDao tipoDao = new TipoItemAcervoDao();
    private final AutorDao autorDao = new AutorDao();
    private final LivroAutorDao livroAutorDao = new LivroAutorDao();
    private int idItem;

    public static void openDetail(int idItem, Stage owner) throws Exception {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(DetalheItemController.class.getResource("/fxml/DetalheItem.fxml"));
        Scene sc = new Scene(loader.load());
        DetalheItemController ctrl = loader.getController();
        Stage st = new Stage();
        st.initOwner(owner);
        st.initModality(Modality.APPLICATION_MODAL);
        st.setScene(sc);
        ctrl.load(idItem);
        st.setTitle("Detalhe Item");
        st.show();
    }

    public void initialize(){
        cmbTipo.setCellFactory(c-> new ListCell<>(){ protected void updateItem(TipoItemAcervo i,boolean e){ super.updateItem(i,e); setText(i==null?"":i.getNome()); }});
        cmbTipo.setButtonCell(new ListCell<>(){ protected void updateItem(TipoItemAcervo i,boolean e){ super.updateItem(i,e); setText(i==null?"":i.getNome()); }});
    }

    public void load(int id){
        try {
            this.idItem = id;
            ItemAcervo it = itemDao.findById(id);
            lblTitulo.setText(it.getTitulo()==null?"":it.getTitulo());
            lblSub.setText(it.getSubtitulo()==null?"":it.getSubtitulo());
            lblAno.setText(it.getAno()==null?"":String.valueOf(it.getAno()));
            lblIdioma.setText(it.getIdioma()==null?"":it.getIdioma());
            txtDesc.setText(it.getDescricao()==null?"":it.getDescricao());
            List<TipoItemAcervo> tipos = tipoDao.findAll();
            cmbTipo.setItems(FXCollections.observableArrayList(tipos));
            if(it.getIdTipo()!=null){
                TipoItemAcervo sel = tipoDao.findById(it.getIdTipo());
                cmbTipo.getSelectionModel().select(sel);
            }
            refreshAutores();
        } catch(Exception e){ showError(e.getMessage()); }
    }

    @FXML private void onSalvarTipo(){
        TipoItemAcervo sel = cmbTipo.getSelectionModel().getSelectedItem();
        try {
            itemDao.updateTipo(idItem, sel==null? -1 : sel.getIdTipo());
            showInfo("Tipo atualizado");
        } catch(Exception e){ showError(e.getMessage()); }
    }

    @FXML private void onVincularAutor(){
        try {
            List<Autor> todos = autorDao.findAll();
            ChoiceDialog<Autor> dlg = new ChoiceDialog<>(todos.isEmpty()?null:todos.get(0), todos);
            dlg.setTitle("Vincular Autor");
            dlg.setHeaderText("Escolha um autor");
            Optional<Autor> opt = dlg.showAndWait();
            opt.ifPresent(a->{
                try { livroAutorDao.vincularAutor(getLivroIdFromItem(idItem), a.getIdAutor(), "autor"); refreshAutores(); } catch(Exception e){ showError(e.getMessage()); }
            });
        } catch(Exception e){ showError(e.getMessage()); }
    }

    @FXML private void onRemoverVinculo(){
        Autor sel = listAutores.getSelectionModel().getSelectedItem();
        if(sel==null) return;
        try { livroAutorDao.removerVinculo(getLivroIdFromItem(idItem), sel.getIdAutor()); refreshAutores(); } catch(Exception e){ showError(e.getMessage()); }
    }

    private void refreshAutores(){
        try {
            int idLivro = getLivroIdFromItem(idItem);
            List<Autor> L = livroAutorDao.listarAutoresDoLivro(idLivro);
            listAutores.setItems(FXCollections.observableArrayList(L));
        } catch(Exception e){ listAutores.setItems(FXCollections.emptyObservableList()); }
    }

    private int getLivroIdFromItem(int idItem) throws Exception {
        // assume id_livro == id_item_acervo per seu script (Livro.id_livro PK references ItemAcervo.id_item_acervo)
        // verify existence
        com.example.SistemaAcervo.dao.LivroDao ld = new com.example.SistemaAcervo.dao.LivroDao();
        com.example.SistemaAcervo.model.Livro l = ld.findById(idItem);
        if(l==null) throw new Exception("Este item não é um livro.");
        return l.getIdLivro();
    }

    private void showError(String m){ Alert a=new Alert(Alert.AlertType.ERROR,m); a.showAndWait(); }
    private void showInfo(String m){ Alert a=new Alert(Alert.AlertType.INFORMATION,m); a.showAndWait(); }
}
