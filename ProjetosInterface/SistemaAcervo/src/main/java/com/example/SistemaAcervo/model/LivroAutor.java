package com.example.SistemaAcervo.model;

public class LivroAutor {
    private Integer idLivroAutor;
    private Integer idLivro;
    private Integer idAutor;
    private String papel;

    public LivroAutor() {}

    public Integer getIdLivroAutor() { return idLivroAutor; }
    public void setIdLivroAutor(Integer idLivroAutor) { this.idLivroAutor = idLivroAutor; }
    public Integer getIdLivro() { return idLivro; }
    public void setIdLivro(Integer idLivro) { this.idLivro = idLivro; }
    public Integer getIdAutor() { return idAutor; }
    public void setIdAutor(Integer idAutor) { this.idAutor = idAutor; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
}
