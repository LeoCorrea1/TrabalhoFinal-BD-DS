// java
package com.example.SistemaBiblioteca.repositor;

import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.conexao.Db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//AQUI ESTÁ O DAO DO ITEM ACERVO QUE CRIEI NO INICIO, TROQUEI PARA REPOSITOR, POREM DEPOIS CONTINUEI COM PACOTE DAO PARA MANTER PADRÃO ( ESSE AINDA É USADO )
public class ItemAcervoDAO {

    public void inserir(ItemAcervo item) {
        String sql = "INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getTitulo());
            stmt.setString(2, item.getSubtitulo());
            stmt.setObject(3, item.getAno());
            stmt.setString(4, item.getIdioma());
            stmt.setString(5, item.getDescricao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir ItemAcervo: " + e.getMessage());
        }
    }

    public ItemAcervo buscarPorId(int id) {
        String sql = "SELECT * FROM ItemAcervo WHERE id_item_acervo = ?";
        ItemAcervo item = null;

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                item = new ItemAcervo(
                        rs.getInt("id_item_acervo"),
                        rs.getString("titulo"),
                        rs.getString("subtitulo"),
                        rs.getInt("ano"),
                        rs.getInt("id_tipo"),
                        rs.getString("idioma"),
                        rs.getString("descricao"),
                        rs.getString("tipo_nome")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar ItemAcervo: " + e.getMessage());
        }

        return item;
    }

    public List<ItemAcervo> listarTodos() {
        List<ItemAcervo> lista = new ArrayList<>();
        String sql = "SELECT * FROM ItemAcervo ORDER BY created_at DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ItemAcervo item = new ItemAcervo(
                        rs.getInt("id_item_acervo"),
                        rs.getString("titulo"),
                        rs.getString("subtitulo"),
                        rs.getInt("ano"),
                        rs.getInt("id_tipo"),
                        rs.getString("idioma"),
                        rs.getString("descricao"),
                        rs.getString("tipo_nome")
                );
                lista.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar ItemAcervo: " + e.getMessage());
        }

        return lista;
    }

    public void atualizar(ItemAcervo item) {
        String sql = """
            UPDATE ItemAcervo SET
            titulo = ?, subtitulo = ?, ano = ?, idioma = ?, descricao = ?,
            updated_at = NOW()
            WHERE id_item_acervo = ?
            """;

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getTitulo());
            stmt.setString(2, item.getSubtitulo());
            stmt.setObject(3, item.getAno());
            stmt.setString(4, item.getIdioma());
            stmt.setString(5, item.getDescricao());
            stmt.setInt(6, item.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar ItemAcervo: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM ItemAcervo WHERE id_item_acervo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar ItemAcervo: " + e.getMessage());
        }
    }
}