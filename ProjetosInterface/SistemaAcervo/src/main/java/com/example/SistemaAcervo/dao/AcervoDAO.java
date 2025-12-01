package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.Acervo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcervoDAO {

    public void inserir(Acervo a) throws SQLException {
        String sql = "INSERT INTO acervo (nome, descricao, categoria, data_item, tipo, responsavel, arquivo_path, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDescricao());
            ps.setString(3, a.getCategoria());
            if (a.getDataItem() != null) ps.setDate(4, Date.valueOf(a.getDataItem()));
            else ps.setNull(4, Types.DATE);
            ps.setString(5, a.getTipo());
            ps.setString(6, a.getResponsavel());
            ps.setString(7, a.getArquivoPath());
            ps.setString(8, a.getStatus() == null ? "ativo" : a.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) a.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(Acervo a) throws SQLException {
        String sql = "UPDATE acervo SET nome=?, descricao=?, categoria=?, data_item=?, tipo=?, responsavel=?, arquivo_path=?, status=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getDescricao());
            ps.setString(3, a.getCategoria());
            if (a.getDataItem() != null) ps.setDate(4, Date.valueOf(a.getDataItem()));
            else ps.setNull(4, Types.DATE);
            ps.setString(5, a.getTipo());
            ps.setString(6, a.getResponsavel());
            ps.setString(7, a.getArquivoPath());
            ps.setString(8, a.getStatus());
            ps.setInt(9, a.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM acervo WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Acervo> listar() throws SQLException {
        List<Acervo> lista = new ArrayList<>();
        String sql = "SELECT * FROM acervo ORDER BY id DESC";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    public List<Acervo> buscar(String termo) throws SQLException {
        List<Acervo> lista = new ArrayList<>();
        String sql = "SELECT * FROM acervo WHERE nome LIKE ? OR categoria LIKE ? OR tipo LIKE ? ORDER BY id DESC";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String t = "%" + termo + "%";
            ps.setString(1, t);
            ps.setString(2, t);
            ps.setString(3, t);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSet(rs));
            }
        }
        return lista;
    }

    private Acervo mapResultSet(ResultSet rs) throws SQLException {
        Acervo a = new Acervo();
        a.setId(rs.getInt("id"));
        a.setNome(rs.getString("nome"));
        a.setDescricao(rs.getString("descricao"));
        a.setCategoria(rs.getString("categoria"));
        Date d = rs.getDate("data_item");
        if (d != null) a.setDataItem(d.toLocalDate());
        a.setTipo(rs.getString("tipo"));
        a.setResponsavel(rs.getString("responsavel"));
        a.setArquivoPath(rs.getString("arquivo_path"));
        a.setStatus(rs.getString("status"));
        return a;
    }
}
