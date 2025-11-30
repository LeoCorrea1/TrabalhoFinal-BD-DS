package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    public Livro findById(int id) throws SQLException {
        String sql = "SELECT id_livro, isbn, edicao, numero_paginas, id_editora FROM Livro WHERE id_livro = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }
    private Livro map(ResultSet rs) throws SQLException {
        Livro l = new Livro();
        l.setId(rs.getInt("id_livro"));
        l.setIsbn(rs.getString("isbn"));
        l.setEdicao(rs.getString("edicao"));
        int np = rs.getInt("numero_paginas");
        if (!rs.wasNull()) l.setNumeroPaginas(np);
        int idEditora = rs.getInt("id_editora");
        if (!rs.wasNull()) l.setIdEditora(idEditora);
        return l;
    }
    public List<Livro> findAll() throws SQLException {
        String sql = "SELECT id_livro, isbn, edicao, numero_paginas, id_editora FROM Livro ORDER BY id_livro DESC";
        List<Livro> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }
    public boolean existsByIsbn(String isbn, Integer idToIgnore) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Livro WHERE isbn = ?";
        if (idToIgnore != null) sql += " AND id_livro <> ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            if (idToIgnore != null) ps.setInt(2, idToIgnore);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    public void insertWithConnection(Connection conn, int idItemAcervo, Livro livro) throws SQLException {
        String sql = "INSERT INTO Livro (id_livro, isbn, edicao, numero_paginas, id_editora) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idItemAcervo);
            ps.setString(2, livro.getIsbn());
            ps.setString(3, livro.getEdicao());
            if (livro.getNumeroPaginas() != null) ps.setInt(4, livro.getNumeroPaginas()); else ps.setNull(4, Types.INTEGER);
            if (livro.getIdEditora() != null) ps.setInt(5, livro.getIdEditora()); else ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
            livro.setId(idItemAcervo); // mantém o model consistente
        }
    }
    public boolean updateWithConnection(Connection conn, Livro livro) throws SQLException {
        String sql = "UPDATE Livro SET isbn=?, edicao=?, numero_paginas=?, id_editora=? WHERE id_livro=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livro.getIsbn());
            ps.setString(2, livro.getEdicao());
            if (livro.getNumeroPaginas() != null) ps.setInt(3, livro.getNumeroPaginas()); else ps.setNull(3, Types.INTEGER);
            if (livro.getIdEditora() != null) ps.setInt(4, livro.getIdEditora()); else ps.setNull(4, Types.INTEGER);
            ps.setInt(5, livro.getId() == null ? -1 : livro.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int idLivro) throws SQLException {
        String sql = "DELETE FROM Livro WHERE id_livro = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLivro);
            return ps.executeUpdate() > 0;
        }
    }
}
