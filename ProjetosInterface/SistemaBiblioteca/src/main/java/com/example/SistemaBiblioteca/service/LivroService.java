
package com.example.SistemaBiblioteca.service;

import com.example.SistemaBiblioteca.conexao.Db;
import com.example.SistemaBiblioteca.dao.ItemAcervoDAO;
import com.example.SistemaBiblioteca.dao.LivroDAO;
import com.example.SistemaBiblioteca.model.ItemAcervo;
import com.example.SistemaBiblioteca.model.Livro;

import java.sql.Connection;
import java.sql.SQLException;

public class LivroService {

    private final ItemAcervoDAO itemDAO = new ItemAcervoDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    /**
     * Cria ItemAcervo + Livro em uma transação.
     * Valida ISBN duplicado antes de inserir (lança SQLException com mensagem se duplicado).
     */
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

                // 1) inserir ItemAcervo (retorna id gerado)
                int idItem = itemDAO.insertWithConnection(conn, item);

                // 2) inserir Livro usando o idItem
                livroDAO.insertWithConnection(conn, idItem, livro);

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex instanceof SQLException ? (SQLException) ex : new SQLException(ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Atualiza ItemAcervo + Livro em transação.
     * Se atualizar o ISBN, valida duplicidade (ignorando o próprio id).
     */
    public void atualizarLivroComItem(ItemAcervo item, Livro livro) throws SQLException {
        // valida ISBN (ignora o próprio id)
        if (livro.getIsbn() != null && !livro.getIsbn().isBlank()) {
            if (livroDAO.existsByIsbn(livro.getIsbn(), livro.getId())) {
                throw new SQLException("ISBN já cadastrado por outro livro: " + livro.getIsbn());
            }
        }

        try (Connection conn = Db.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // atualiza ItemAcervo
                boolean ok1 = itemDAO.updateWithConnection(conn, item);
                // atualiza Livro
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
