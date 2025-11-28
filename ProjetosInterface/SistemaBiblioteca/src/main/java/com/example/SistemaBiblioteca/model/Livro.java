package com.example.SistemaBiblioteca.model;

public class Livro {

        private int id; // mesma PK do ItemAcervo
        private String isbn;
        private String edicao;
        private Integer numeroPaginas;
        private Integer idEditora;

        private ItemAcervo itemAcervo; // relação 1–1

        public Livro() {}

        public Livro(int id, String isbn, String edicao, Integer numeroPaginas, Integer idEditora) {
            this.id = id;
            this.isbn = isbn;
            this.edicao = edicao;
            this.numeroPaginas = numeroPaginas;
            this.idEditora = idEditora;
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public Integer getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(Integer numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public Integer getIdEditora() {
        return idEditora;
    }

    public void setIdEditora(Integer idEditora) {
        this.idEditora = idEditora;
    }

    public ItemAcervo getItemAcervo() {
        return itemAcervo;
    }

    public void setItemAcervo(ItemAcervo itemAcervo) {
        this.itemAcervo = itemAcervo;
    }
}


