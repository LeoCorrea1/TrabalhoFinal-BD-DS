package com.example.SistemaBiblioteca.model;

public class Exemplar {

    private Integer idExemplar;
    private Integer idItemAcervo;
    private String codigoBarras;
    private String estadoConservacao;
    private Boolean disponivel;
    private Integer idLocalizacao;

    // novo campo para mostrar o nome/descrição da localização
    private String nomeLocalizacao;

    public Integer getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Integer idExemplar) { this.idExemplar = idExemplar; }

    public Integer getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(Integer idItemAcervo) { this.idItemAcervo = idItemAcervo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getEstadoConservacao() { return estadoConservacao; }
    public void setEstadoConservacao(String estadoConservacao) { this.estadoConservacao = estadoConservacao; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }

    public Integer getIdLocalizacao() { return idLocalizacao; }
    public void setIdLocalizacao(Integer idLocalizacao) { this.idLocalizacao = idLocalizacao; }

    public String getNomeLocalizacao() { return nomeLocalizacao; }
    public void setNomeLocalizacao(String nomeLocalizacao) { this.nomeLocalizacao = nomeLocalizacao; }

    @Override
    public String toString() {
        return "Exemplar{" + idExemplar + " - " + codigoBarras + "}";
    }
}
