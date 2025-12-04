-- 1. livros publicados apos 2010 sobre tema especifico
SELECT ia.titulo, ia.ano, l.isbn, e.nome AS editora
FROM ItemAcervo ia
JOIN Livro l ON ia.id_item_acervo = l.id_livro
JOIN Editora e ON l.id_editora = e.id_editora
JOIN ItemAssunto ias ON ia.id_item_acervo = ias.id_item_acervo
JOIN Assunto a ON ias.id_assunto = a.id_assunto
WHERE ia.ano > 2010 AND a.descricao LIKE '%Historia%'
ORDER BY ia.ano DESC;

-- 2. autor com mais livros cadastrado
SELECT a.nome, a.sobrenome, COUNT(la.id_livro) AS total_livros
FROM Autor a
JOIN LivroAutor la ON a.id_autor = la.id_autor
GROUP BY a.id_autor
HAVING COUNT(la.id_livro) >= ALL (
    SELECT COUNT(id_livro) FROM LivroAutor GROUP BY id_autor
);

-- 3. documentos acervo historico
SELECT t.nome AS tipo, ia.titulo, ia.ano, ex.codigo_barras
FROM ItemAcervo ia
JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
LEFT JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
WHERE t.nome IN ('Carta', 'Fotografia', 'Objeto Histórico')
ORDER BY t.nome;

-- 4. itens sem exemplar disponivel
SELECT ia.titulo, t.nome AS tipo, COUNT(ex.id_exemplar) AS exemplares
FROM ItemAcervo ia
JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
LEFT JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo AND ex.disponivel = TRUE
GROUP BY ia.id_item_acervo
HAVING COUNT(ex.id_exemplar) = 0;

-- 5. ranking usuarios mais emprestimos
SELECT u.nome, u.tipo_usuario, COUNT(e.id_emprestimo) AS total
FROM Usuario u
JOIN Emprestimo e ON u.id_usuario = e.id_usuario
GROUP BY u.id_usuario
HAVING COUNT(e.id_emprestimo) >= 1
ORDER BY total DESC;

-- 6. itens mais emprestado
SELECT ia.titulo, COUNT(e.id_emprestimo) AS vezes
FROM ItemAcervo ia
JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
JOIN Emprestimo e ON ex.id_exemplar = e.id_exemplar
GROUP BY ia.id_item_acervo
HAVING COUNT(e.id_emprestimo) >= 1
ORDER BY vezes DESC
LIMIT 10;

-- 7. emprestimos atrazados
SELECT u.nome, ia.titulo, e.data_prevista_devolucao,
       DATEDIFF(CURRENT_DATE, e.data_prevista_devolucao) AS dias_atraso
FROM Emprestimo e
JOIN Usuario u ON e.id_usuario = u.id_usuario
JOIN Exemplar ex ON e.id_exemplar = ex.id_exemplar
JOIN ItemAcervo ia ON ex.id_item_acervo = ia.id_item_acervo
WHERE e.data_devolucao IS NULL AND e.data_prevista_devolucao < CURRENT_DATE
ORDER BY dias_atraso DESC;

-- 8. estatisticas por tipo e assunto
SELECT t.nome AS tipo, a.descricao AS assunto, COUNT(DISTINCT ia.id_item_acervo) AS qtd
FROM TipoItemAcervo t
JOIN ItemAcervo ia ON t.id_tipo = ia.id_tipo
LEFT JOIN ItemAssunto ias ON ia.id_item_acervo = ias.id_item_acervo
LEFT JOIN Assunto a ON ias.id_assunto = a.id_assunto
GROUP BY t.nome, a.descricao
HAVING COUNT(DISTINCT ia.id_item_acervo) >= 1
ORDER BY qtd DESC;

-- 9. livros por editora e autores
SELECT e.nome AS editora, ia.titulo, COUNT(la.id_autor) AS qtd_autores
FROM Editora e
JOIN Livro l ON e.id_editora = l.id_editora
JOIN ItemAcervo ia ON l.id_livro = ia.id_item_acervo
LEFT JOIN LivroAutor la ON l.id_livro = la.id_livro
GROUP BY e.id_editora, ia.id_item_acervo
HAVING COUNT(la.id_autor) >= 1
ORDER BY e.nome;

-- 10. movimentacoes por mes
SELECT DATE_FORMAT(m.data_hora, '%Y-%m') AS mes, m.tipo, COUNT(*) AS total
FROM Movimentacao m
JOIN ItemAcervo ia ON m.id_item_acervo = ia.id_item_acervo
WHERE m.data_hora >= DATE_SUB(CURRENT_DATE, INTERVAL 12 MONTH)
GROUP BY mes, m.tipo
HAVING COUNT(*) >= 1
ORDER BY mes DESC;
