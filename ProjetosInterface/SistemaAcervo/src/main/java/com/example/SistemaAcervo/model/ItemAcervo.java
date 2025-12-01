package com.example.SistemaAcervo.model;

public class ItemAcervo {
    private Integer idItemAcervo;
    private String titulo;
    private String subtitulo;
    private Integer ano;
    private String idioma;
    private String descricao;
    private Integer idTipo;

    public Integer getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(Integer id) { this.idItemAcervo = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }
    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String s) { this.subtitulo = s; }
    public Integer getAno() { return ano; }
    public void setAno(Integer a) { this.ano = a; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String i) { this.idioma = i; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String d) { this.descricao = d; }
    public Integer getIdTipo() { return idTipo; }
    public void setIdTipo(Integer t) { this.idTipo = t; }
}
