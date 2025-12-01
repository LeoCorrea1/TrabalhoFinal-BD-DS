package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.Livro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDao {

    public Livro findById(int id) throws SQLException {
        String sql = "SELECT * FROM Livro WHERE id_livro=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Livro l = new Livro();
                    l.setIdLivro(rs.getInt("id_livro"));
                    l.setIsbn(rs.getString("isbn"));
                    l.setEdicao(rs.getString("edicao"));
                    l.setNumeroPaginas(rs.getInt("numero_paginas"));
                    l.setIdEditora(rs.getInt("id_editora"));
                    return l;
                }
            }
        }
        return null;
    }

    public List<Livro> findAll() throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM Livro";
        try (Connection c = Db.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Livro l = new Livro();
                l.setIdLivro(rs.getInt("id_livro"));
                l.setIsbn(rs.getString("isbn"));
                l.setEdicao(rs.getString("edicao"));
                l.setNumeroPaginas(rs.getInt("numero_paginas"));
                l.setIdEditora(rs.getInt("id_editora"));
                lista.add(l);
            }
        }
        return lista;
    }

    // Busca itens do acervo que são do tipo "Livro"
    public List<LivroComTitulo> findAllComTitulo() throws SQLException {
        List<LivroComTitulo> lista = new ArrayList<>();

        // Filtra pelo nome do tipo = 'Livro'
        String sql = "SELECT i.id_item_acervo, i.titulo " +
                "FROM ItemAcervo i " +
                "INNER JOIN TipoItemAcervo t ON i.id_tipo = t.id_tipo " +
                "WHERE LOWER(t.nome) = 'livro' " +
                "ORDER BY i.titulo";

        try (Connection c = Db.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LivroComTitulo l = new LivroComTitulo();
                l.setIdLivro(rs.getInt("id_item_acervo"));
                l.setIsbn(""); // Não tem ISBN nessa abordagem
                l.setTitulo(rs.getString("titulo"));
                lista.add(l);
            }
        }
        return lista;
    }

    // Classe interna para livro com título
    public static class LivroComTitulo {
        private int idLivro;
        private String isbn;
        private String titulo;

        public int getIdLivro() { return idLivro; }
        public void setIdLivro(int idLivro) { this.idLivro = idLivro; }
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }

        @Override
        public String toString() {
            return titulo + (isbn != null && !isbn.isEmpty() ? " (ISBN: " + isbn + ")" : "");
        }
    }
}