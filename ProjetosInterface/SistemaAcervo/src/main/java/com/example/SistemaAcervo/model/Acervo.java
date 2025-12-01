package com.example.SistemaAcervo.model;

import java.time.LocalDate;

public class Acervo {
    private int id;
    private String nome;
    private String descricao;
    private String categoria;
    private LocalDate dataItem;
    private String tipo;
    private String responsavel;
    private String arquivoPath;
    private String status;

    public Acervo() {}

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getDataItem() { return dataItem; }
    public void setDataItem(LocalDate dataItem) { this.dataItem = dataItem; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getArquivoPath() { return arquivoPath; }
    public void setArquivoPath(String arquivoPath) { this.arquivoPath = arquivoPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
