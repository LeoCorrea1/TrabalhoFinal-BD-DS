package com.example.SistemaAcervo.model;

public class Livro {
    private int idLivro;
    private String isbn;
    private String edicao;
    private Integer numeroPaginas;
    private int idEditora;

    public int getIdLivro() { return idLivro; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public int getIdEditora() { return idEditora; }
    public void setIdEditora(int idEditora) { this.idEditora = idEditora; }
}
