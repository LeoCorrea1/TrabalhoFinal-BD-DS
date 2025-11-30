package com.example.SistemaBiblioteca.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Model ItemAcervo compatível com TableView e com DAO. 
 * Adicionado campos para exibição: isbn, edicao, editoraNome.
 */
public class ItemAcervo {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty titulo = new SimpleStringProperty();
    private final StringProperty subtitulo = new SimpleStringProperty();
    private final IntegerProperty ano = new SimpleIntegerProperty();
    private final IntegerProperty idTipo = new SimpleIntegerProperty();
    private final StringProperty idioma = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final StringProperty tipoNome = new SimpleStringProperty();

    // novos campos para lista
    private final StringProperty isbn = new SimpleStringProperty();
    private final StringProperty edicao = new SimpleStringProperty();
    private final StringProperty editoraNome = new SimpleStringProperty();

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

    // --- properties (para TableColumn lambda) ---
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty idItemAcervoProperty() { return id; } // alias
    public StringProperty tituloProperty() { return titulo; }
    public StringProperty subtituloProperty() { return subtitulo; }
    public IntegerProperty anoProperty() { return ano; }
    public StringProperty idiomaProperty() { return idioma; }
    public StringProperty descricaoProperty() { return descricao; }
    public StringProperty tipoNomeProperty() { return tipoNome; }
    public StringProperty isbnProperty() { return isbn; }
    public StringProperty edicaoProperty() { return edicao; }
    public StringProperty editoraNomeProperty() { return editoraNome; }

    // --- getters / setters tradicionais (usados por DAO/serviços) ---
    public Integer getId() { int v = id.get(); return v == 0 ? null : v; }
    public int getIdPrimitive() { return id.get(); }
    public void setId(Integer value) { this.id.set(value == null ? 0 : value); }
    public void setId(int value) { this.id.set(value); }
    public int getIdItemAcervo() { return getIdPrimitive(); }
    public void setIdItemAcervo(int id) { setId(id); }

    public String getTitulo() { return titulo.get(); }
    public void setTitulo(String value) { this.titulo.set(value == null ? "" : value); }

    public String getSubtitulo() { return subtitulo.get(); }
    public void setSubtitulo(String value) { this.subtitulo.set(value == null ? "" : value); }

    public Integer getAno() { int v = ano.get(); return v == 0 ? null : v; }
    public void setAno(Integer value) { this.ano.set(value == null ? 0 : value); }

    public Integer getIdTipo() { int v = idTipo.get(); return v == 0 ? null : v; }
    public void setIdTipo(Integer value) { this.idTipo.set(value == null ? 0 : value); }

    public String getIdioma() { return idioma.get(); }
    public void setIdioma(String value) { this.idioma.set(value == null ? "" : value); }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String value) { this.descricao.set(value == null ? "" : value); }

    public String getTipoNome() { return tipoNome.get(); }
    public void setTipoNome(String value) { this.tipoNome.set(value == null ? "" : value); }

    // novos campos
    public String getIsbn() { return isbn.get(); }
    public void setIsbn(String value) { this.isbn.set(value == null ? "" : value); }

    public String getEdicao() { return edicao.get(); }
    public void setEdicao(String value) { this.edicao.set(value == null ? "" : value); }

    public String getEditoraNome() { return editoraNome.get(); }
    public void setEditoraNome(String value) { this.editoraNome.set(value == null ? "" : value); }

    @Override
    public String toString() {
        return "ItemAcervo{" + getId() + " - " + getTitulo() + "}";
    }
}
