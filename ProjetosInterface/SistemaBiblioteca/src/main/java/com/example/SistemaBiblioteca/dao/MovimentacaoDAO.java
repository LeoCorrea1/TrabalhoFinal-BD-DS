package com.example.SistemaBiblioteca.dao;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.model.Movimentacao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {
    //AJUDA DA IA
    public void registrar(Integer idItemAcervo, Integer idExemplar, Integer idUsuario,
                          String tipo, String descricao) {
        String sql = """
            INSERT INTO Movimentacao (id_item_acervo, id_exemplar, id_usuario, tipo, descricao, data_hora)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, idItemAcervo);
            ps.setObject(2, idExemplar);
            ps.setObject(3, idUsuario);
            ps.setString(4, tipo);
            ps.setString(5, descricao);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    public List<Movimentacao> findAll() {
        List<Movimentacao> lista = new ArrayList<>();
        String sql = """
            SELECT m.*, u.nome AS nome_usuario, e.codigo_barras
              FROM Movimentacao m
              LEFT JOIN Usuario u ON u.id_usuario = m.id_usuario
              LEFT JOIN Exemplar e ON e.id_exemplar = m.id_exemplar
             ORDER BY m.data_hora DESC
        """;
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movimentacao m = new Movimentacao();
                m.setIdMov(rs.getInt("id_mov"));
                m.setIdItemAcervo((Integer) rs.getObject("id_item_acervo"));
                m.setIdExemplar((Integer) rs.getObject("id_exemplar"));
                m.setIdUsuario((Integer) rs.getObject("id_usuario"));
                m.setTipo(rs.getString("tipo"));
                m.setDescricao(rs.getString("descricao"));
                m.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                try { m.setNomeUsuario(rs.getString("nome_usuario")); } catch (SQLException ignored) {}
                try { m.setCodigoExemplar(rs.getString("codigo_barras")); } catch (SQLException ignored) {}
                lista.add(m);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}