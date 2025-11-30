package com.example.SistemaBiblioteca.model;

public class TipoItemAcervo {
    private Integer idTipo;
    private String nome;
    private String descricao;

    public TipoItemAcervo() {}

    public TipoItemAcervo(Integer idTipo, String nome, String descricao) {
        this.idTipo = idTipo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getIdTipo() { return idTipo; }
    public void setIdTipo(Integer idTipo) { this.idTipo = idTipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return nome;
    }
}
