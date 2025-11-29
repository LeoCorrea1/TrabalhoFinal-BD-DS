package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> findAll() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY id_usuario DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTelefone(rs.getString("telefone"));
                u.setTipoUsuario(rs.getString("tipo_usuario"));
                lista.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void insert(Usuario u) {
        String sql = "INSERT INTO Usuario (nome, email, telefone, tipo_usuario) VALUES (?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getTelefone());
            st.setString(4, u.getTipoUsuario());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Usuario u) {
        String sql = """
                UPDATE Usuario
                SET nome=?, email=?, telefone=?, tipo_usuario=?
                WHERE id_usuario=?
                """;

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setString(1, u.getNome());
            st.setString(2, u.getEmail());
            st.setString(3, u.getTelefone());
            st.setString(4, u.getTipoUsuario());
            st.setInt(5, u.getIdUsuario());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
