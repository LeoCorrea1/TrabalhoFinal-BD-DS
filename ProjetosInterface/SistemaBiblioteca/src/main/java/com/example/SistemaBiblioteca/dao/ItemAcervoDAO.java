package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.ItemAcervo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//PRECISEI MUIT AJUDA DA IA
public class ItemAcervoDAO {

    private ItemAcervo map(ResultSet rs) throws SQLException {
        ItemAcervo it = new ItemAcervo();
        it.setIdItemAcervo(rs.getInt("id_item_acervo"));
        it.setTitulo(rs.getString("titulo"));
        it.setSubtitulo(rs.getString("subtitulo"));
        int ano = rs.getInt("ano");
        it.setAno(rs.wasNull() ? null : ano);
        it.setIdioma(rs.getString("idioma"));
        it.setDescricao(rs.getString("descricao"));
        int idTipo = rs.getInt("id_tipo");
        it.setIdTipo(rs.wasNull() ? null : idTipo);
        it.setTipoNome(rs.getString("tipo_nome"));
        try {
            it.setIsbn(rs.getString("isbn"));
        } catch (SQLException ignore) {}
        try {
            it.setEdicao(rs.getString("edicao"));
        } catch (SQLException ignore) {}
        try {
            it.setEditoraNome(rs.getString("editora_nome"));
        } catch (SQLException ignore) {}

        return it;
    }

    public List<ItemAcervo> findAll() {
        String sql = """
                SELECT ia.id_item_acervo, ia.titulo, ia.subtitulo, ia.ano, ia.idioma, ia.descricao,
                       ia.id_tipo, t.nome AS tipo_nome,
                       l.isbn, l.edicao, e.nome AS editora_nome
                FROM ItemAcervo ia
                LEFT JOIN TipoItemAcervo t ON t.id_tipo = ia.id_tipo
                LEFT JOIN Livro l ON l.id_livro = ia.id_item_acervo
                LEFT JOIN Editora e ON e.id_editora = l.id_editora
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

    public ItemAcervo findById(int id) {
        String sql = """
                SELECT ia.id_item_acervo, ia.titulo, ia.subtitulo, ia.ano, ia.idioma, ia.descricao,
                       ia.id_tipo, t.nome AS tipo_nome,
                       l.isbn, l.edicao, e.nome AS editora_nome
                FROM ItemAcervo ia
                LEFT JOIN TipoItemAcervo t ON t.id_tipo = ia.id_tipo
                LEFT JOIN Livro l ON l.id_livro = ia.id_item_acervo
                LEFT JOIN Editora e ON e.id_editora = l.id_editora
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
        String sql = "INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao, id_tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            if (it.getAno() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, it.getAno());
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            if (it.getIdTipo() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, it.getIdTipo());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rk = ps.getGeneratedKeys()) {
                    if (rk.next()) {
                        int generated = rk.getInt(1);
                        it.setId(generated);
                        return generated;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean update(ItemAcervo it) {
        String sql = "UPDATE ItemAcervo SET titulo=?, subtitulo=?, ano=?, idioma=?, descricao=?, id_tipo=? WHERE id_item_acervo=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            if (it.getAno() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, it.getAno());
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            if (it.getIdTipo() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, it.getIdTipo());
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
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteIfNoDependencies(int id) {
        String sql = "DELETE FROM ItemAcervo WHERE id_item_acervo = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException fkEx) {
            // violação de FK -> existem dependências (ex.: Exemplares, Empréstimos, etc)
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int insertWithConnection(Connection conn, ItemAcervo it) throws SQLException {
        String sql = "INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao, id_tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            if (it.getAno() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, it.getAno());
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            if (it.getIdTipo() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, it.getIdTipo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
    public boolean updateWithConnection(Connection conn, ItemAcervo it) throws SQLException {
        String sql = "UPDATE ItemAcervo SET titulo=?, subtitulo=?, ano=?, idioma=?, descricao=?, id_tipo=? WHERE id_item_acervo=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, it.getTitulo());
            ps.setString(2, it.getSubtitulo());
            if (it.getAno() == null) ps.setNull(3, Types.INTEGER); else ps.setInt(3, it.getAno());
            ps.setString(4, it.getIdioma());
            ps.setString(5, it.getDescricao());
            if (it.getIdTipo() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, it.getIdTipo());
            ps.setInt(7, it.getIdItemAcervo());
            return ps.executeUpdate() > 0;
        }
    }
}
