// java
package com.example.SistemaBiblioteca.repositor;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Livro;
import java.sql.*;

public class LivroDAO {

    public void inserir(Livro livro) {
        String sql = """
        INSERT INTO Livro (id_livro, isbn, edicao, numero_paginas, id_editora)
        VALUES (?, ?, ?, ?, ?)
    """;

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, livro.getId());
            stmt.setString(2, livro.getIsbn());
            stmt.setString(3, livro.getEdicao());
            stmt.setObject(4, livro.getNumeroPaginas());
            stmt.setObject(5, livro.getIdEditora());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir Livro: " + e.getMessage());
        }
    }

    public Livro buscarPorId(int id) {
        String sql = "SELECT * FROM Livro WHERE id_livro = ?";
        Livro livro = null;

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setEdicao(rs.getString("edicao"));
                livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                livro.setIdEditora(rs.getInt("id_editora"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Livro: " + e.getMessage());
        }
        return livro;
    }

    public Livro buscarPorISBN(String isbn) {
        String sql = "SELECT * FROM Livro WHERE isbn = ?";
        Livro livro = null;

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getInt("id_livro"));
                livro.setIsbn(rs.getString("isbn"));
                livro.setEdicao(rs.getString("edicao"));
                livro.setNumeroPaginas(rs.getInt("numero_paginas"));
                livro.setIdEditora(rs.getInt("id_editora"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar Livro por ISBN: " + e.getMessage());
        }
        return livro;
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Livro WHERE id_livro = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao deletar Livro: " + e.getMessage());
        }
    }
}
