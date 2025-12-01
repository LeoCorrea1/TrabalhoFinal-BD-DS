package com.example.SistemaAcervo.dao;

import com.example.SistemaAcervo.conexao.Db;
import com.example.SistemaAcervo.model.TipoItemAcervo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoItemAcervoDao {

    public List<TipoItemAcervo> findAll() throws SQLException {
        List<TipoItemAcervo> list = new ArrayList<>();
        String sql = "SELECT * FROM TipoItemAcervo";
        try(Connection c = Db.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)){
            while(rs.next()){
                TipoItemAcervo t = new TipoItemAcervo();
                t.setIdTipo(rs.getInt("id_tipo"));
                t.setNome(rs.getString("nome"));
                t.setDescricao(rs.getString("descricao"));
                list.add(t);
            }
        }
        return list;
    }

    public TipoItemAcervo findById(int id) throws SQLException {
        String sql = "SELECT * FROM TipoItemAcervo WHERE id_tipo=?";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    TipoItemAcervo t = new TipoItemAcervo();
                    t.setIdTipo(rs.getInt("id_tipo"));
                    t.setNome(rs.getString("nome"));
                    t.setDescricao(rs.getString("descricao"));
                    return t;
                }
            }
        }
        return null;
    }

    public void insert(TipoItemAcervo t) throws SQLException {
        String sql = "INSERT INTO TipoItemAcervo(nome, descricao) VALUES (?,?)";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,t.getNome());
            ps.setString(2,t.getDescricao());
            ps.executeUpdate();
        }
    }

    public void update(TipoItemAcervo t) throws SQLException {
        String sql = "UPDATE TipoItemAcervo SET nome=?, descricao=? WHERE id_tipo=?";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,t.getNome());
            ps.setString(2,t.getDescricao());
            ps.setInt(3,t.getIdTipo());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM TipoItemAcervo WHERE id_tipo=?";
        try(Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }
}
