package com.example.SistemaBiblioteca.model;

public class ItemAcervo {
    private Integer idItemAcervo;
    private String titulo;
    private String subtitulo;
    private Integer ano;
    private String idioma;
    private String descricao;
    private Integer idTipo;
    private String tipoNome;

    public ItemAcervo() {}

    public Integer getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(Integer idItemAcervo) { this.idItemAcervo = idItemAcervo; }

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

    public Integer getIdTipo() { return idTipo; }
    public void setIdTipo(Integer idTipo) { this.idTipo = idTipo; }

    public String getTipoNome() { return tipoNome; }
    public void setTipoNome(String tipoNome) { this.tipoNome = tipoNome; }

    @Override
    public String toString() {
        return "ItemAcervo{" + idItemAcervo + ", " + titulo + "}";
    }
}
