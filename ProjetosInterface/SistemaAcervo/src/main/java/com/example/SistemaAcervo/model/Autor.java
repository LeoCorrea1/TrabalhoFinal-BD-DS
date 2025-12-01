package com.example.SistemaAcervo.model;

public class Autor {
    private int idAutor;
    private String nome;
    private String sobrenome;
    private String nacionalidade;

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    @Override
    public String toString() {
        if (sobrenome == null || sobrenome.isBlank()) return nome;
        return nome + " " + sobrenome;
    }
}
