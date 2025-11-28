package com.example.projetofxcombd.model;

import javafx.beans.property.*;

public class ItemAcervo {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titulo = new SimpleStringProperty();
    private final StringProperty subtitulo = new SimpleStringProperty();
    private final IntegerProperty ano = new SimpleIntegerProperty();
    private final StringProperty idioma = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final StringProperty createdAt = new SimpleStringProperty();
    private final StringProperty updatedAt = new SimpleStringProperty();

    public ItemAcervo() {}

    public ItemAcervo(int id, String titulo, String subtitulo, int ano, String idioma, String descricao, String createdAt, String updatedAt) {
        this.id.set(id);
        this.titulo.set(titulo);
        this.subtitulo.set(subtitulo);
        this.ano.set(ano);
        this.idioma.set(idioma);
        this.descricao.set(descricao);
        this.createdAt.set(createdAt);
        this.updatedAt.set(updatedAt);
    }

    // GETTERS NORMAIS
    public int getId() { return id.get(); }
    public String getTitulo() { return titulo.get(); }
    public String getSubtitulo() { return subtitulo.get(); }
    public int getAno() { return ano.get(); }
    public String getIdioma() { return idioma.get(); }
    public String getDescricao() { return descricao.get(); }
    public String getCreatedAt() { return createdAt.get(); }
    public String getUpdatedAt() { return updatedAt.get(); }

    // SETTERS NORMAIS
    public void setId(int value) { id.set(value); }
    public void setTitulo(String value) { titulo.set(value); }
    public void setSubtitulo(String value) { subtitulo.set(value); }
    public void setAno(int value) { ano.set(value); }
    public void setIdioma(String value) { idioma.set(value); }
    public void setDescricao(String value) { descricao.set(value); }
    public void setCreatedAt(String value) { createdAt.set(value); }
    public void setUpdatedAt(String value) { updatedAt.set(value); }

    // PROPERTIES PARA O TABLEVIEW
    public IntegerProperty idProperty() { return id; }
    public StringProperty tituloProperty() { return titulo; }
    public StringProperty subtituloProperty() { return subtitulo; }
    public IntegerProperty anoProperty() { return ano; }
    public StringProperty idiomaProperty() { return idioma; }
    public StringProperty descricaoProperty() { return descricao; }
    public StringProperty createdAtProperty() { return createdAt; }
    public StringProperty updatedAtProperty() { return updatedAt; }
}
