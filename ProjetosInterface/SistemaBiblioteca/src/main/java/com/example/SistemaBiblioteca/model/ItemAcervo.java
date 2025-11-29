
package com.example.SistemaBiblioteca.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemAcervo {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titulo = new SimpleStringProperty();
    private final StringProperty subtitulo = new SimpleStringProperty();
    private final IntegerProperty ano = new SimpleIntegerProperty();
    private final IntegerProperty idTipo = new SimpleIntegerProperty();
    private final StringProperty idioma = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final StringProperty tipoNome = new SimpleStringProperty();

    public ItemAcervo() {}

    public ItemAcervo(int id, String titulo, String subtitulo, int ano, int idTipo, String idioma, String descricao, String tipoNome) {
        this.id.set(id);
        this.titulo.set(titulo);
        this.subtitulo.set(subtitulo);
        this.ano.set(ano);
        this.idTipo.set(idTipo);
        this.idioma.set(idioma);
        this.descricao.set(descricao);
        this.tipoNome.set(tipoNome);
    }

    // id / id_item_acervo
    public IntegerProperty idProperty() {
        return id;
    }
    // alias usado pelo controller / FXML (PropertyValueFactory("idItemAcervo"))
    public IntegerProperty idItemAcervoProperty() {
        return id;
    }
    public int getId() {
        return id.get();
    }
    // alias usado nas chamadas sel.getIdItemAcervo()
    public int getIdItemAcervo() {
        return getId();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    // alias
    public void setIdItemAcervo(int id) {
        setId(id);
    }

    // titulo
    public StringProperty tituloProperty() {
        return titulo;
    }
    public String getTitulo() {
        return titulo.get();
    }
    public void setTitulo(String titulo) {
        this.titulo.set(titulo);
    }

    // subtitulo
    public StringProperty subtituloProperty() {
        return subtitulo;
    }
    public String getSubtitulo() {
        return subtitulo.get();
    }
    public void setSubtitulo(String subtitulo) {
        this.subtitulo.set(subtitulo);
    }

    // ano
    public IntegerProperty anoProperty() {
        return ano;
    }
    public int getAno() {
        return ano.get();
    }
    public void setAno(int ano) {
        this.ano.set(ano);
    }

    // idTipo (id_tipo) - usado pelo DAO
    public IntegerProperty idTipoProperty() {
        return idTipo;
    }
    public int getIdTipo() {
        return idTipo.get();
    }
    public void setIdTipo(int idTipo) {
        this.idTipo.set(idTipo);
    }

    // idioma
    public StringProperty idiomaProperty() {
        return idioma;
    }
    public String getIdioma() {
        return idioma.get();
    }
    public void setIdioma(String idioma) {
        this.idioma.set(idioma);
    }

    // descricao
    public StringProperty descricaoProperty() {
        return descricao;
    }
    public String getDescricao() {
        return descricao.get();
    }
    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    // tipoNome (nome do tipo) - usado pelo DAO/joins
    public StringProperty tipoNomeProperty() {
        return tipoNome;
    }
    public String getTipoNome() {
        return tipoNome.get();
    }
    public void setTipoNome(String tipoNome) {
        this.tipoNome.set(tipoNome);
    }
}