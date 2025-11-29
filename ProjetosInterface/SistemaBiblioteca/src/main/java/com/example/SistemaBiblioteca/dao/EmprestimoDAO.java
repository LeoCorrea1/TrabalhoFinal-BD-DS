package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Emprestimo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    private Emprestimo map(ResultSet rs) throws SQLException {
        return new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_exemplar"),
                rs.getInt("id_usuario"),
                rs.getTimestamp("data_emprestimo").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getTimestamp("data_devolucao") != null ? rs.getTimestamp("data_devolucao").toLocalDateTime() : null,
                rs.getString("status")
        );
    }

    public List<Emprestimo> findAll() {
        List<Emprestimo> list = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimo ORDER BY data_emprestimo DESC";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Emprestimo findById(int id) {
        String sql = "SELECT * FROM Emprestimo WHERE id_emprestimo = ?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Emprestimo e) {
        String sql = "INSERT INTO Emprestimo (id_exemplar, id_usuario, data_emprestimo, data_prevista_devolucao, status) VALUES (?,?,?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getIdExemplar());
            ps.setInt(2, e.getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDataEmprestimo()));
            ps.setTimestamp(4, Timestamp.valueOf(e.getDataPrevistaDevolucao()));
            ps.setString(5, e.getStatus());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet g = ps.getGeneratedKeys()) {
                    if (g.next()) e.setIdEmprestimo(g.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean update(Emprestimo e) {
        String sql = "UPDATE Emprestimo SET id_exemplar=?, id_usuario=?, data_emprestimo=?, data_prevista_devolucao=?, data_devolucao=?, status=? WHERE id_emprestimo=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getIdExemplar());
            ps.setInt(2, e.getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDataEmprestimo()));
            ps.setTimestamp(4, Timestamp.valueOf(e.getDataPrevistaDevolucao()));
            ps.setTimestamp(5, e.getDataDevolucao() != null ? Timestamp.valueOf(e.getDataDevolucao()) : null);
            ps.setString(6, e.getStatus());
            ps.setInt(7, e.getIdEmprestimo());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM Emprestimo WHERE id_emprestimo=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }
}