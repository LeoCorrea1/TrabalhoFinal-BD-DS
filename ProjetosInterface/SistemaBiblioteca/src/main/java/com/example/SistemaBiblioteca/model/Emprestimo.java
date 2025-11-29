package com.example.SistemaBiblioteca.model;

import java.time.LocalDateTime;

public class Emprestimo {
    private Integer idEmprestimo;
    private Integer idExemplar;
    private Integer idUsuario;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataPrevistaDevolucao;
    private LocalDateTime dataDevolucao;
    private String status;

    public Emprestimo() {}

    public Emprestimo(Integer id, Integer idExemplar, Integer idUsuario,
                      LocalDateTime dataEmprestimo, LocalDateTime dataPrevistaDevolucao,
                      LocalDateTime dataDevolucao, String status) {
        this.idEmprestimo = id;
        this.idExemplar = idExemplar;
        this.idUsuario = idUsuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    // Getters / setters
    public Integer getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(Integer idEmprestimo) { this.idEmprestimo = idEmprestimo; }

    public Integer getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Integer idExemplar) { this.idExemplar = idExemplar; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDateTime getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDateTime d) { this.dataPrevistaDevolucao = d; }

    public LocalDateTime getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDateTime dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}