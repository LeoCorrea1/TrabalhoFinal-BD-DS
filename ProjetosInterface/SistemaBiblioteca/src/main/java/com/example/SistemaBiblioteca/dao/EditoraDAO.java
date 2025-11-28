package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Editora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditoraDAO {

    public List<Editora> findAll() {
        return search("");
    }

    /**
     * Procura por editoras cujo nome contenha a string (LIKE %nome%).
     */
    public List<Editora> search(String nome) {
        List<Editora> list = new ArrayList<>();
        String sql = "SELECT id_editora, nome, contato, endereco FROM Editora WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + (nome == null ? "" : nome) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Editora e = new Editora(
                            rs.getInt("id_editora"),
                            rs.getString("nome"),
                            rs.getString("contato"),
                            rs.getString("endereco")
                    );
                    list.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean insert(Editora e) {
        String sql = "INSERT INTO Editora (nome, contato, endereco) VALUES (?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getContato());
            ps.setString(3, e.getEndereco());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet g = ps.getGeneratedKeys()) {
                    if (g.next()) e.setId(g.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean update(Editora e) {
        String sql = "UPDATE Editora SET nome = ?, contato = ?, endereco = ? WHERE id_editora = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getContato());
            ps.setString(3, e.getEndereco());
            ps.setInt(4, e.getId() == null ? -1 : e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Tenta deletar; se houver dependências (FK) o método captura SQLException e retorna false.
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM Editora WHERE id_editora = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            // provável violação de FK (livro -> editora)
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Busca uma editora por id.
     */
    public Editora findById(int id) {
        String sql = "SELECT id_editora, nome, contato, endereco FROM Editora WHERE id_editora = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Editora(rs.getInt("id_editora"), rs.getString("nome"), rs.getString("contato"), rs.getString("endereco"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
