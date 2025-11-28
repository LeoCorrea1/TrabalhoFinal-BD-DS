package com.example.SistemaBiblioteca.model;

public class Livro {
    private Integer id; // igual ao idItemAcervo
    private String isbn;
    private String edicao;
    private Integer numeroPaginas;
    private Integer idEditora;

    public Livro() {}

    // getters/setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getEdicao() { return edicao; }
    public void setEdicao(String edicao) { this.edicao = edicao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public Integer getIdEditora() { return idEditora; }
    public void setIdEditora(Integer idEditora) { this.idEditora = idEditora; }
}
