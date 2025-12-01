package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.Autor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDao {

    public List<Autor> findAll() throws SQLException {
        String sql = "SELECT id_autor, nome, sobrenome, nacionalidade FROM Autor ORDER BY nome";
        try (Connection c = Db.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            List<Autor> lista = new ArrayList<>();
            while (r.next()) {
                Autor a = new Autor();
                a.setIdAutor(r.getInt("id_autor"));
                a.setNome(r.getString("nome"));
                a.setSobrenome(r.getString("sobrenome"));
                a.setNacionalidade(r.getString("nacionalidade"));
                lista.add(a);
            }
            return lista;
        }
    }

    public Autor findById(int id) throws SQLException {
        String sql = "SELECT id_autor, nome, sobrenome, nacionalidade FROM Autor WHERE id_autor=?";
        try (Connection c = Db.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    Autor a = new Autor();
                    a.setIdAutor(r.getInt("id_autor"));
                    a.setNome(r.getString("nome"));
                    a.setSobrenome(r.getString("sobrenome"));
                    a.setNacionalidade(r.getString("nacionalidade"));
                    return a;
                }
                return null;
            }
        }
    }

    public Autor insert(Autor a) throws SQLException {
        String sql = "INSERT INTO Autor(nome, sobrenome, nacionalidade) VALUES(?,?,?)";
        try (Connection c = Db.getConnection();
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, a.getNome());
            p.setString(2, a.getSobrenome());
            p.setString(3, a.getNacionalidade());
            p.executeUpdate();
            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) a.setIdAutor(r.getInt(1));
            }
            return a;
        }
    }

    public void update(Autor a) throws SQLException {
        String sql = "UPDATE Autor SET nome=?, sobrenome=?, nacionalidade=? WHERE id_autor=?";
        try (Connection c = Db.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, a.getNome());
            p.setString(2, a.getSobrenome());
            p.setString(3, a.getNacionalidade());
            p.setInt(4, a.getIdAutor());
            p.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Autor WHERE id_autor=?";
        try (Connection c = Db.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            p.executeUpdate();
        }
    }
}