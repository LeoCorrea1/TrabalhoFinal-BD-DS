package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.Autor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroAutorDao {

    public void vincularAutor(int idLivro, int idAutor, String papel) throws SQLException {
        String sql = "INSERT INTO LivroAutor(id_livro,id_autor,papel) VALUES (?,?,?)";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,idLivro);
            ps.setInt(2,idAutor);
            ps.setString(3,papel);
            ps.executeUpdate();
        }
    }

    public void removerVinculo(int idLivro, int idAutor) throws SQLException {
        String sql = "DELETE FROM LivroAutor WHERE id_livro=? AND id_autor=?";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,idLivro);
            ps.setInt(2,idAutor);
            ps.executeUpdate();
        }
    }

    public List<Autor> listarAutoresDoLivro(int idLivro) throws SQLException {
        List<Autor> list = new ArrayList<>();
        String sql = "SELECT a.* FROM Autor a JOIN LivroAutor la ON a.id_autor=la.id_autor WHERE la.id_livro=?";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,idLivro);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Autor a = new Autor();
                    a.setIdAutor(rs.getInt("id_autor"));
                    a.setNome(rs.getString("nome"));
                    a.setSobrenome(rs.getString("sobrenome"));
                    a.setNacionalidade(rs.getString("nacionalidade"));
                    list.add(a);
                }
            }
        }
        return list;
    }
}
