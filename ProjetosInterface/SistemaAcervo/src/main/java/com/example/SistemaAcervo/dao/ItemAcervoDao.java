package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.ItemAcervo;
import com.example.SistemaAcervo.model.ItemAcervoCompleto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemAcervoDao {

    public List<ItemAcervo> findAll() throws SQLException {
        List<ItemAcervo> list = new ArrayList<>();
        String sql = "SELECT * FROM ItemAcervo ORDER BY titulo";
        try (Connection c = Db.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ItemAcervo i = new ItemAcervo();
                i.setIdItemAcervo(rs.getInt("id_item_acervo"));
                i.setTitulo(rs.getString("titulo"));
                i.setSubtitulo(rs.getString("subtitulo"));
                i.setAno(rs.getInt("ano"));
                i.setIdioma(rs.getString("idioma"));
                i.setDescricao(rs.getString("descricao"));
                i.setIdTipo(rs.getObject("id_tipo") != null ? rs.getInt("id_tipo") : null);
                list.add(i);
            }
        }
        return list;
    }

    public ItemAcervo findById(int id) throws SQLException {
        String sql = "SELECT * FROM ItemAcervo WHERE id_item_acervo=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ItemAcervo i = new ItemAcervo();
                    i.setIdItemAcervo(rs.getInt("id_item_acervo"));
                    i.setTitulo(rs.getString("titulo"));
                    i.setSubtitulo(rs.getString("subtitulo"));
                    i.setAno(rs.getInt("ano"));
                    i.setIdioma(rs.getString("idioma"));
                    i.setDescricao(rs.getString("descricao"));
                    i.setIdTipo(rs.getObject("id_tipo") != null ? rs.getInt("id_tipo") : null);
                    return i;
                }
            }
        }
        return null;
    }

    public void updateTipo(int idItem, int idTipo) throws SQLException {
        String sql = "UPDATE ItemAcervo SET id_tipo=? WHERE id_item_acervo=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            if (idTipo == -1) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, idTipo);
            ps.setInt(2, idItem);
            ps.executeUpdate();
        }
    }

    // NOVO: Busca item com todas as informações
    public ItemAcervoCompleto findByIdCompleto(int id) throws SQLException {
        ItemAcervoCompleto item = new ItemAcervoCompleto();

        // 1. Buscar dados básicos + tipo
        String sqlBasico = """
            SELECT i.*, t.nome as tipo_nome
            FROM ItemAcervo i
            LEFT JOIN TipoItemAcervo t ON i.id_tipo = t.id_tipo
            WHERE i.id_item_acervo = ?
            """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sqlBasico)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item.setIdItemAcervo(rs.getInt("id_item_acervo"));
                    item.setTitulo(rs.getString("titulo"));
                    item.setSubtitulo(rs.getString("subtitulo"));
                    item.setAno(rs.getObject("ano") != null ? rs.getInt("ano") : null);
                    item.setIdioma(rs.getString("idioma"));
                    item.setDescricao(rs.getString("descricao"));
                    item.setTipoNome(rs.getString("tipo_nome"));
                }
            }
        }

        // 2. Verificar se é livro e buscar dados específicos
        String sqlLivro = """
            SELECT l.*, e.nome as editora_nome
            FROM Livro l
            LEFT JOIN Editora e ON l.id_editora = e.id_editora
            WHERE l.id_livro = ?
            """;

        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sqlLivro)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    item.setEhLivro(true);
                    item.setIsbn(rs.getString("isbn"));
                    item.setEdicao(rs.getString("edicao"));
                    item.setNumeroPaginas(rs.getObject("numero_paginas") != null ? rs.getInt("numero_paginas") : null);
                    item.setEditoraNome(rs.getString("editora_nome"));
                } else {
                    item.setEhLivro(false);
                }
            }
        }

        // 3. Buscar autores (se for livro)
        if (item.isEhLivro()) {
            String sqlAutores = """
                SELECT a.nome, a.sobrenome, la.papel
                FROM Autor a
                INNER JOIN LivroAutor la ON a.id_autor = la.id_autor
                WHERE la.id_livro = ?
                ORDER BY a.nome
                """;

            try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sqlAutores)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String nome = rs.getString("nome");
                        String sobrenome = rs.getString("sobrenome");
                        String papel = rs.getString("papel");

                        String autorCompleto = nome;
                        if (sobrenome != null && !sobrenome.isEmpty()) {
                            autorCompleto += " " + sobrenome;
                        }
                        if (papel != null && !papel.isEmpty()) {
                            autorCompleto += " (" + papel + ")";
                        }
                        item.addAutor(autorCompleto);
                    }
                }
            }
        }

        return item;
    }
}