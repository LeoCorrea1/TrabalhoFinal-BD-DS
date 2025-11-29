package com.example.SistemaBiblioteca.model;

import java.time.LocalDateTime;

public class Reserva {

    private Integer idReserva;
    private Integer idExemplar;
    private Integer idUsuario;
    private LocalDateTime dataReserva;
    private LocalDateTime dataExpiracao;
    private String status;

    // somente para exibição na tabela
    private String nomeUsuario;
    private String codigoExemplar;

    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public Integer getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Integer idExemplar) { this.idExemplar = idExemplar; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getDataReserva() { return dataReserva; }
    public void setDataReserva(LocalDateTime dataReserva) { this.dataReserva = dataReserva; }

    public LocalDateTime getDataExpiracao() { return dataExpiracao; }
    public void setDataExpiracao(LocalDateTime dataExpiracao) { this.dataExpiracao = dataExpiracao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getCodigoExemplar() { return codigoExemplar; }
    public void setCodigoExemplar(String codigoExemplar) { this.codigoExemplar = codigoExemplar; }
}