package com.example.SistemaBiblioteca.model;

import java.time.LocalDateTime;

public class Movimentacao {

    private Integer idMov;
    private Integer idItemAcervo;
    private Integer idExemplar;
    private Integer idUsuario;
    private String tipo;
    private String descricao;
    private LocalDateTime dataHora;
    //AJUDA DA IA
    private String nomeUsuario;
    private String codigoExemplar;

    public Integer getIdMov() { return idMov; }
    public void setIdMov(Integer idMov) { this.idMov = idMov; }

    public Integer getIdItemAcervo() { return idItemAcervo; }
    public void setIdItemAcervo(Integer idItemAcervo) { this.idItemAcervo = idItemAcervo; }

    public Integer getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Integer idExemplar) { this.idExemplar = idExemplar; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getCodigoExemplar() { return codigoExemplar; }
    public void setCodigoExemplar(String codigoExemplar) { this.codigoExemplar = codigoExemplar; }
}