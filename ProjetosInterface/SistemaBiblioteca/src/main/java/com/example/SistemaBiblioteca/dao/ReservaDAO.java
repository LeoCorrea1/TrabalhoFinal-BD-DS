package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Reserva;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    private Reserva map(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(rs.getInt("id_reserva"));
        r.setIdExemplar(rs.getInt("id_exemplar"));
        r.setIdUsuario(rs.getInt("id_usuario"));
        r.setDataReserva(rs.getTimestamp("data_reserva").toLocalDateTime());
        r.setDataExpiracao(rs.getTimestamp("data_expiracao").toLocalDateTime());
        r.setStatus(rs.getString("status"));
        try { r.setNomeUsuario(rs.getString("nome_usuario")); } catch (SQLException ignored) {}
        try { r.setCodigoExemplar(rs.getString("codigo_barras")); } catch (SQLException ignored) {}
        return r;
    }

    /** Lista todas as reservas com usuário e exemplar */
    public List<Reserva> findAllComNomes() {
        List<Reserva> list = new ArrayList<>();
        String sql = """
            SELECT r.*, u.nome AS nome_usuario, e.codigo_barras
              FROM Reserva r
              JOIN Usuario u   ON u.id_usuario = r.id_usuario
              JOIN Exemplar e  ON e.id_exemplar = r.id_exemplar
              ORDER BY r.data_reserva DESC
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    /** Verifica se o exemplar tem reserva ativa e ainda válida */
    public boolean existeReservaAtivaOuValida(int idExemplar) {
        String sql = """
            SELECT COUNT(*) FROM Reserva
             WHERE id_exemplar = ?
               AND status = 'ativa'
               AND data_expiracao > NOW()
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idExemplar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /** Insere nova reserva e marca exemplar como indisponível */
    public boolean insert(Reserva r) {
        String sql = """
            INSERT INTO Reserva (id_exemplar, id_usuario, data_reserva, data_expiracao, status)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getIdExemplar());
            ps.setInt(2, r.getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataReserva()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getDataExpiracao()));
            ps.setString(5, r.getStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet g = ps.getGeneratedKeys()) {
                    if (g.next()) r.setIdReserva(g.getInt(1));
                }
                atualizarDisponibilidadeExemplar(r.getIdExemplar(), false);
                return true;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Atualiza reserva existente */
    public boolean update(Reserva r) {
        String sql = """
            UPDATE Reserva
               SET id_exemplar=?, id_usuario=?, data_reserva=?, 
                   data_expiracao=?, status=? 
             WHERE id_reserva=?
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, r.getIdExemplar());
            ps.setInt(2, r.getIdUsuario());
            ps.setTimestamp(3, Timestamp.valueOf(r.getDataReserva()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getDataExpiracao()));
            ps.setString(5, r.getStatus());
            ps.setInt(6, r.getIdReserva());

            boolean ok = ps.executeUpdate() > 0;
            if (ok && ("cancelada".equalsIgnoreCase(r.getStatus()) || "expirada".equalsIgnoreCase(r.getStatus())))
                atualizarDisponibilidadeExemplar(r.getIdExemplar(), true);
            return ok;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Exclui reserva e libera exemplar */
    public boolean delete(int idReserva, int idExemplar) {
        String sql = "DELETE FROM Reserva WHERE id_reserva=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) atualizarDisponibilidadeExemplar(idExemplar, true);
            return ok;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    /** Atualiza disponibilidade de exemplar */
    private void atualizarDisponibilidadeExemplar(int idExemplar, boolean disponivel) {
        String sql = "UPDATE Exemplar SET disponivel=? WHERE id_exemplar=?";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, disponivel);
            ps.setInt(2, idExemplar);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /** Marca reservas com prazo vencido como expiradas */
    public void expirarReservasVencidas() {
        String sql = """
            UPDATE Reserva
               SET status='expirada'
             WHERE status='ativa' AND data_expiracao < NOW()
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}