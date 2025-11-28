package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.TipoItemAcervo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoItemAcervoDAO {

    public List<TipoItemAcervo> findAll() throws SQLException {
        String sql = "SELECT id_tipo, nome, descricao FROM TipoItemAcervo ORDER BY id_tipo";
        List<TipoItemAcervo> lista = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoItemAcervo t = new TipoItemAcervo();
                t.setIdTipo(rs.getInt("id_tipo"));
                t.setNome(rs.getString("nome"));
                t.setDescricao(rs.getString("descricao"));
                lista.add(t);
            }
        }
        return lista;
    }

    public TipoItemAcervo findById(int id) throws SQLException {
        String sql = "SELECT id_tipo, nome, descricao FROM TipoItemAcervo WHERE id_tipo = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoItemAcervo t = new TipoItemAcervo();
                    t.setIdTipo(rs.getInt("id_tipo"));
                    t.setNome(rs.getString("nome"));
                    t.setDescricao(rs.getString("descricao"));
                    return t;
                } else return null;
            }
        }
    }

    public int insert(TipoItemAcervo tipo) throws SQLException {
        String sql = "INSERT INTO TipoItemAcervo (nome, descricao) VALUES (?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo.getNome());
            ps.setString(2, tipo.getDescricao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}
