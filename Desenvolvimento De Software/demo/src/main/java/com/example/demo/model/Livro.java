package com.example.demo.model;

public class Livro {
    private int id;
    private String titulo;
    private int anoPublicacao;
    private String isbn;
    private int idAutor;
    private int idAssunto;

    public Livro() {}

    public Livro(int id, String titulo, int anoPublicacao, String isbn, int idAutor, int idAssunto) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.isbn = isbn;
        this.idAutor = idAutor;
        this.idAssunto = idAssunto;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public int getIdAssunto() { return idAssunto; }
    public void setIdAssunto(int idAssunto) { this.idAssunto = idAssunto; }
}
