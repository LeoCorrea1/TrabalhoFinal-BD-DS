package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Exemplar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO {

    public List<Exemplar> findByItem(int idItemAcervo) {
        List<Exemplar> list = new ArrayList<>();

        String sql = """
                SELECT e.id_exemplar,
                       e.id_item_acervo,
                       e.codigo_barras,
                       e.estado_conservacao,
                       e.disponivel,
                       e.id_localizacao,
                       CONCAT_WS(' / ', l.setor, l.prateleira, l.caixa) AS local_nome
                FROM Exemplar e
                LEFT JOIN Localizacao l ON l.id_localizacao = e.id_localizacao
                WHERE e.id_item_acervo = ?
                ORDER BY e.id_exemplar DESC
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, idItemAcervo);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Exemplar e = new Exemplar();
                    e.setIdExemplar(rs.getInt("id_exemplar"));
                    e.setIdItemAcervo(rs.getInt("id_item_acervo"));
                    e.setCodigoBarras(rs.getString("codigo_barras"));
                    e.setEstadoConservacao(rs.getString("estado_conservacao"));
                    e.setDisponivel(rs.getObject("disponivel") == null ? null : rs.getBoolean("disponivel"));
                    Object idLocObj = rs.getObject("id_localizacao");
                    e.setIdLocalizacao(idLocObj == null ? null : rs.getInt("id_localizacao"));
                    e.setNomeLocalizacao(rs.getString("local_nome"));
                    list.add(e);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean insert(Exemplar e) {
        String sql = """
                INSERT INTO Exemplar
                (id_item_acervo, codigo_barras, estado_conservacao, disponivel, id_localizacao)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, e.getIdItemAcervo());
            st.setString(2, e.getCodigoBarras());
            st.setString(3, e.getEstadoConservacao());
            if (e.getDisponivel() == null) st.setNull(4, Types.BOOLEAN);
            else st.setBoolean(4, e.getDisponivel());
            if (e.getIdLocalizacao() == null) st.setNull(5, Types.INTEGER);
            else st.setInt(5, e.getIdLocalizacao());

            int affected = st.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) e.setIdExemplar(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean update(Exemplar e) {
        String sql = """
                UPDATE Exemplar SET
                codigo_barras = ?, estado_conservacao = ?, disponivel = ?, id_localizacao = ?
                WHERE id_exemplar = ?
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, e.getCodigoBarras());
            st.setString(2, e.getEstadoConservacao());
            if (e.getDisponivel() == null) st.setNull(3, Types.BOOLEAN);
            else st.setBoolean(3, e.getDisponivel());
            if (e.getIdLocalizacao() == null) st.setNull(4, Types.INTEGER);
            else st.setInt(4, e.getIdLocalizacao());
            st.setInt(5, e.getIdExemplar());

            return st.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idExemplar) {
        String sql = "DELETE FROM Exemplar WHERE id_exemplar = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, idExemplar);
            return st.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
