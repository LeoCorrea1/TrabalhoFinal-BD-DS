package com.example.SistemaAcervo.model;

public class TipoItemAcervo {
    private Integer idTipo;
    private String nome;
    private String descricao;

    public Integer getIdTipo() { return idTipo; }
    public void setIdTipo(Integer id) { this.idTipo = id; }
    public String getNome() { return nome; }
    public void setNome(String n) { this.nome = n; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String d) { this.descricao = d; }

    @Override
    public String toString(){ return nome; }
}
