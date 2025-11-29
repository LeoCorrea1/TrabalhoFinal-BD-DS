package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.ItemAcervo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemAcervoDAO {

    private ItemAcervo map(ResultSet rs) throws SQLException {
        ItemAcervo it = new ItemAcervo();
        it.setIdItemAcervo(rs.getInt("id_item_acervo"));
        it.setTitulo(rs.getString("titulo"));
        it.setSubtitulo(rs.getString("subtitulo"));
        it.setAno(rs.getInt("ano"));
        it.setIdioma(rs.getString("idioma"));
        it.setDescricao(rs.getString("descricao"));
        it.setIdTipo(rs.getInt("id_tipo"));
        it.setTipoNome(rs.getString("tipo_nome"));
        return it;
    }

    public List<ItemAcervo> findAll() {
        String sql = """
                SELECT ia.*, t.nome AS tipo_nome
                FROM ItemAcervo ia
                LEFT JOIN TipoItemAcervo t ON t.id_tipo = ia.id_tipo
                ORDER BY ia.id_item_acervo DESC
                """;

        List<ItemAcervo> lista = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<ItemAcervo> search(String termo) {
        if (termo == null || termo.isBlank()) {
            return findAll();
        }

        String sql = """
                SELECT ia.*, t.nome AS tipo_nome
                FROM ItemAcervo ia
                LEFT JOIN TipoItemAcervo t ON t.id_tipo = ia.id_tipo
                WHERE ia.titulo LIKE ?
                ORDER BY ia.id_item_acervo DESC
                """;

        List<ItemAcervo> lista = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + termo + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ItemAcervo findById(int id) {
        String sql = """
                SELECT ia.*, t.nome AS tipo_nome
                FROM ItemAcervo ia
                LEFT JOIN TipoItemAcervo t ON t.id_tipo = ia.id_tipo
                WHERE ia.id_item_acervo = ?
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int insert(ItemAcervo it) {
        String sql = """
                INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao, id_tipo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            ps.setObject(3, it.getAno(), java.sql.Types.INTEGER);
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            ps.setObject(6, it.getIdTipo(), java.sql.Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean update(ItemAcervo it) {
        String sql = """
                UPDATE ItemAcervo
                SET titulo=?, subtitulo=?, ano=?, idioma=?, descricao=?, id_tipo=?
                WHERE id_item_acervo=?
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            ps.setObject(3, it.getAno(), java.sql.Types.INTEGER);
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            ps.setObject(6, it.getIdTipo(), java.sql.Types.INTEGER);
            ps.setInt(7, it.getIdItemAcervo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM ItemAcervo WHERE id_item_acervo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao deletar ItemAcervo: " + e.getMessage());
        }
        return false;
    }
    public int insertWithConnection(Connection conn, ItemAcervo it) throws SQLException {
        String sql = """
            INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao, id_tipo)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            ps.setObject(3, it.getAno(), java.sql.Types.INTEGER);
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            ps.setObject(6, it.getIdTipo(), java.sql.Types.INTEGER);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return -1;
    }

    public boolean updateWithConnection(Connection conn, ItemAcervo it) throws SQLException {
        String sql = """
            UPDATE ItemAcervo
            SET titulo=?, subtitulo=?, ano=?, idioma=?, descricao=?, id_tipo=?
            WHERE id_item_acervo=?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            ps.setObject(3, it.getAno(), java.sql.Types.INTEGER);
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            ps.setObject(6, it.getIdTipo(), java.sql.Types.INTEGER);
            ps.setInt(7, it.getIdItemAcervo());

            return ps.executeUpdate() > 0;
        }
    }
    public boolean deleteIfNoDependencies(int id) {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM ItemAcervo WHERE id_item_acervo = ?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;  // se falhar por FK, retorna false

        } catch (SQLException e) {
            return false;
        }
    }

}