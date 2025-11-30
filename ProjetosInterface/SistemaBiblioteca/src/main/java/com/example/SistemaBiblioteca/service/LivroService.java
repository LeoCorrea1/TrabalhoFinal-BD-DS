package com.example.SistemaBiblioteca.service;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.dao.ItemAcervoDAO;
import com.example.SistemaBiblioteca.dao.LivroDAO;
import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.model.Livro;

import java.sql.Connection;
import java.sql.SQLException;

//IA GEROU ( ESTAVA COM MUITA DIFICULDADE NO ITEM ACERVO ,ENTAO PEDI AJUDA )
public class LivroService {

    private final ItemAcervoDAO itemDAO = new ItemAcervoDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    private Integer extractItemId(ItemAcervo item) {
        if (item == null) return null;
        try {
            // tenta getIdItemAcervo()
            try {
                java.lang.reflect.Method m = item.getClass().getMethod("getIdItemAcervo");
                Object r = m.invoke(item);
                if (r instanceof Integer) return (Integer) r;
            } catch (NoSuchMethodException ignored) {}

            // tenta getId()
            try {
                java.lang.reflect.Method m2 = item.getClass().getMethod("getId");
                Object r2 = m2.invoke(item);
                if (r2 instanceof Integer) return (Integer) r2;
            } catch (NoSuchMethodException ignored) {}

        } catch (Throwable t) {
            // não quebrar por reflexão
        }
        return null;
    }

    public void criarLivroComItem(ItemAcervo item, Livro livro) throws SQLException {
        // valida ISBN
        if (livro.getIsbn() != null && !livro.getIsbn().isBlank()) {
            if (livroDAO.existsByIsbn(livro.getIsbn(), null)) {
                throw new SQLException("ISBN já cadastrado: " + livro.getIsbn());
            }
        }

        try (Connection conn = Db.getConnection()) {
            try {
                conn.setAutoCommit(false);

                Integer existingId = extractItemId(item);
                int idItem;
                if (existingId != null && existingId > 0) {
                    // Item já existe: opcionalmente atualiza o item (não substitui)
                    try {
                        itemDAO.updateWithConnection(conn, item);
                    } catch (Exception ignore) {}
                    idItem = existingId;
                    livroDAO.insertWithConnection(conn, idItem, livro);
                    livro.setId(idItem);
                } else {
                    // Item não existe: inserir e usar id gerado
                    idItem = itemDAO.insertWithConnection(conn, item);
                    if (idItem <= 0) throw new SQLException("Falha ao inserir ItemAcervo (id inválido).");
                    livroDAO.insertWithConnection(conn, idItem, livro);
                    livro.setId(idItem);
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex instanceof SQLException ? (SQLException) ex : new SQLException(ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void atualizarLivroComItem(ItemAcervo item, Livro livro) throws SQLException {
        if (livro.getIsbn() != null && !livro.getIsbn().isBlank()) {
            if (livroDAO.existsByIsbn(livro.getIsbn(), livro.getId())) {
                throw new SQLException("ISBN já cadastrado por outro livro: " + livro.getIsbn());
            }
        }

        try (Connection conn = Db.getConnection()) {
            try {
                conn.setAutoCommit(false);

                boolean ok1 = itemDAO.updateWithConnection(conn, item);
                boolean ok2 = livroDAO.updateWithConnection(conn, livro);

                if (!ok1 || !ok2) {
                    conn.rollback();
                    throw new SQLException("Falha ao atualizar item/livro (uma das operações não afetou linhas).");
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex instanceof SQLException ? (SQLException) ex : new SQLException(ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
