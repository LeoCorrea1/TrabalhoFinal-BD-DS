package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Emprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    /* ---------- Mapeamento de um registro ---------- */
    private Emprestimo map(ResultSet rs) throws SQLException {
        Emprestimo e = new Emprestimo(
                rs.getInt("id_emprestimo"),
                rs.getInt("id_exemplar"),
                rs.getInt("id_usuario"),
                rs.getTimestamp("data_emprestimo").toLocalDateTime(),
                rs.getTimestamp("data_prevista_devolucao").toLocalDateTime(),
                rs.getTimestamp("data_devolucao") == null ? null :
                        rs.getTimestamp("data_devolucao").toLocalDateTime(),
                rs.getString("status")
        );
        try { e.setNomeUsuario(rs.getString("nome_usuario")); } catch (SQLException ignored) {}
        try { e.setTituloExemplar(rs.getString("titulo_exemplar")); } catch (SQLException ignored) {}
        return e;
    }

    /* ---------- Listagem com JOINs para exibir nomes ---------- */
    public List<Emprestimo> findAllComNomes() {
        List<Emprestimo> list = new ArrayList<>();
        String sql = """
            SELECT e.*, u.nome AS nome_usuario, i.titulo AS titulo_exemplar
              FROM Emprestimo e
              JOIN Usuario u   ON u.id_usuario = e.id_usuario
              JOIN Exemplar ex ON ex.id_exemplar = e.id_exemplar
              JOIN ItemAcervo i ON i.id_item_acervo = ex.id_item_acervo
              ORDER BY e.data_emprestimo DESC
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    /* ---------- INSERT ---------- */
    public boolean insert(Emprestimo e) {
        String sql = """
            INSERT INTO Emprestimo
                (id_exemplar, id_usuario, data_emprestimo,
                 data_prevista_devolucao, status)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {
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

    /* ---------- UPDATE ---------- */
    public boolean update(Emprestimo e) {
        String sql = """
            UPDATE Emprestimo
               SET id_exemplar=?,
                   id_usuario=?,
                   data_emprestimo=?,
                   data_prevista_devolucao=?,
                   data_devolucao=?,
                   status=?
             WHERE id_emprestimo=?
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getIdExemplar());
            ps.setInt(2, e.getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(e.getDataEmprestimo()));
            ps.setTimestamp(4, Timestamp.valueOf(e.getDataPrevistaDevolucao()));
            if (e.getDataDevolucao() != null)
                ps.setTimestamp(5, Timestamp.valueOf(e.getDataDevolucao()));
            else
                ps.setNull(5, Types.TIMESTAMP);
            ps.setString(6, e.getStatus());
            ps.setInt(7, e.getIdEmprestimo());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    /* ---------- DELETE ---------- */
    public boolean delete(int id) {
        String sql = "DELETE FROM Emprestimo WHERE id_emprestimo=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }
}