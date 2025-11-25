package com.example.demo.dao;

import com.example.demo.Conexao.Conexao;
import com.example.demo.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // CREATE
    public boolean inserir(Livro livro) {
        String sql = "INSERT INTO Livro (titulo, anoPublicacao, isbn, idAutor, idAssunto) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getIdAutor());
            stmt.setInt(5, livro.getIdAssunto());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ (listar todos)
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Livro";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("anoPublicacao"),
                        rs.getString("isbn"),
                        rs.getInt("idAutor"),
                        rs.getInt("idAssunto")
                );
                lista.add(livro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // READ (buscar por ID)
    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM Livro WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Livro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("anoPublicacao"),
                        rs.getString("isbn"),
                        rs.getInt("idAutor"),
                        rs.getInt("idAssunto")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE
    public boolean atualizar(Livro livro) {
        String sql = "UPDATE Livro SET titulo=?, anoPublicacao=?, isbn=?, idAutor=?, idAssunto=? " +
                "WHERE id=?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getTitulo());
            stmt.setInt(2, livro.getAnoPublicacao());
            stmt.setString(3, livro.getIsbn());
            stmt.setInt(4, livro.getIdAutor());
            stmt.setInt(5, livro.getIdAssunto());
            stmt.setInt(6, livro.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deletar(int id) {
        String sql = "DELETE FROM Livro WHERE id=?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
