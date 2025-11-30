package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Localizacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalizacaoDAO {

    public List<Localizacao> findAll() {
        String sql = "SELECT * FROM Localizacao ORDER BY id_localizacao DESC";
        List<Localizacao> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Localizacao findById(int id) {
        String sql = "SELECT * FROM Localizacao WHERE id_localizacao = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Localizacao> search(String termo) {
        if (termo == null || termo.isBlank()) return findAll();
        String sql = "SELECT * FROM Localizacao WHERE setor LIKE ? OR prateleira LIKE ? OR caixa LIKE ? ORDER BY id_localizacao DESC";
        List<Localizacao> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String t = "%" + termo + "%";
            ps.setString(1, t);
            ps.setString(2, t);
            ps.setString(3, t);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Localizacao loc) {
        String sql = "INSERT INTO Localizacao (setor, prateleira, caixa) VALUES (?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, loc.getSetor());
            ps.setString(2, loc.getPrateleira());
            ps.setString(3, loc.getCaixa());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) loc.setIdLocalizacao(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Localizacao loc) {
        String sql = "UPDATE Localizacao SET setor=?, prateleira=?, caixa=? WHERE id_localizacao=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loc.getSetor());
            ps.setString(2, loc.getPrateleira());
            ps.setString(3, loc.getCaixa());
            ps.setInt(4, loc.getIdLocalizacao() == null ? -1 : loc.getIdLocalizacao());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Localizacao WHERE id_localizacao = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Localizacao map(ResultSet rs) throws SQLException {
        Localizacao l = new Localizacao();
        l.setIdLocalizacao(rs.getInt("id_localizacao"));
        l.setSetor(rs.getString("setor"));
        l.setPrateleira(rs.getString("prateleira"));
        l.setCaixa(rs.getString("caixa"));
        return l;
    }
}
