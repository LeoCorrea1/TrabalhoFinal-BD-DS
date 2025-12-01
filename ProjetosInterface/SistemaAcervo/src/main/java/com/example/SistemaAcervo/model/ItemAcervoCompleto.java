package com.example.SistemaAcervo.model;

import java.util.ArrayList;
import java.util.List;

public class ItemAcervoCompleto {

    // Dados básicos do ItemAcervo
    private Integer idItemAcervo;
    private String titulo;
    private String subtitulo;
    private Integer ano;
    private String idioma;
    private String descricao;
    private String tipoNome;

    // Dados específicos de Livro
    private boolean ehLivro;
    private String isbn;
    private String edicao;
    private Integer numeroPaginas;
    private String editoraNome;

    // Autores
    private List<String> autores = new ArrayList<>();

    // Getters e Setters
    public Integer getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(Integer id) { this.idItemAcervo = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getTipoNome() { return tipoNome; }
    public void setTipoNome(String tipoNome) { this.tipoNome = tipoNome; }

    public boolean isEhLivro() { return ehLivro; }
    public void setEhLivro(boolean ehLivro) { this.ehLivro = ehLivro; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public String getEditoraNome() { return editoraNome; }
    public void setEditoraNome(String editoraNome) { this.editoraNome = editoraNome; }

    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }

    public void addAutor(String autor) { this.autores.add(autor); }
}