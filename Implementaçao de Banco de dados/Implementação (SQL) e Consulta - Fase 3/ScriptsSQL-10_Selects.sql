
-- 1. livros publicados apos 2010 sobre tema especifico
SELECT 
    ia.titulo,
    ia.ano,
    l.isbn,
    l.edicao,
    e.nome AS editora,
    GROUP_CONCAT(a.descricao SEPARATOR ', ') AS assuntos
FROM ItemAcervo ia
INNER JOIN Livro l ON ia.id_item_acervo = l.id_livro
INNER JOIN Editora e ON l.id_editora = e.id_editora
INNER JOIN ItemAssunto ias ON ia.id_item_acervo = ias.id_item_acervo
INNER JOIN Assunto a ON ias.id_assunto = a.id_assunto
WHERE ia.ano > 2010
  AND a.descricao LIKE '%História%'
GROUP BY ia.id_item_acervo, ia.titulo, ia.ano, l.isbn, l.edicao, e.nome
ORDER BY ia.ano DESC;

-- 2. autor com mais livros cadastrados
SELECT 
    a.id_autor,
    CONCAT(a.nome, ' ', COALESCE(a.sobrenome, '')) AS nome_completo,
    a.nacionalidade,
    COUNT(la.id_livro) AS total_livros
FROM Autor a
INNER JOIN LivroAutor la ON a.id_autor = la.id_autor
GROUP BY a.id_autor, a.nome, a.sobrenome, a.nacionalidade
HAVING COUNT(la.id_livro) = (
    SELECT MAX(contagem)
    FROM (
        SELECT COUNT(id_livro) AS contagem
        FROM LivroAutor
        GROUP BY id_autor
    ) AS subconsulta
)
ORDER BY total_livros DESC;

-- 3. documentos do acervo historico com detalhes
SELECT 
    t.nome AS tipo_item,
    ia.titulo,
    ia.ano,
    CASE 
        WHEN c.id_item_acervo IS NOT NULL THEN CONCAT('De: ', c.remetente, ' Para: ', c.destinatario)
        WHEN f.id_item_acervo IS NOT NULL THEN CONCAT('Fotógrafo: ', f.fotografo, ' - Local: ', f.local)
        WHEN o.id_item_acervo IS NOT NULL THEN CONCAT('Material: ', o.material, ' - Procedência: ', o.procedencia)
    END AS detalhes_especificos,
    ex.codigo_barras,
    loc.setor
FROM ItemAcervo ia
INNER JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
LEFT JOIN Carta c ON ia.id_item_acervo = c.id_item_acervo
LEFT JOIN Fotografia f ON ia.id_item_acervo = f.id_item_acervo
LEFT JOIN ObjetoHistorico o ON ia.id_item_acervo = o.id_item_acervo
LEFT JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
LEFT JOIN Localizacao loc ON ex.id_localizacao = loc.id_localizacao
WHERE t.nome IN ('Carta', 'Fotografia', 'Objeto Histórico')
ORDER BY t.nome, ia.ano;

-- 4. itens sem exemplares disponiveis
SELECT 
    ia.id_item_acervo,
    ia.titulo,
    t.nome AS tipo,
    ia.ano,
    COUNT(ex.id_exemplar) AS total_exemplares,
    SUM(CASE WHEN ex.disponivel = TRUE THEN 1 ELSE 0 END) AS disponiveis
FROM ItemAcervo ia
INNER JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
LEFT JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
GROUP BY ia.id_item_acervo, ia.titulo, t.nome, ia.ano
HAVING SUM(CASE WHEN ex.disponivel = TRUE THEN 1 ELSE 0 END) = 0
    OR COUNT(ex.id_exemplar) = 0
ORDER BY ia.titulo;

-- 5. ranking usuarios com mais emprestimos
SELECT 
    u.tipo_usuario,
    u.nome AS usuario,
    u.email,
    COUNT(e.id_emprestimo) AS total_emprestimos,
    SUM(CASE WHEN e.status = 'devolvido' THEN 1 ELSE 0 END) AS devolvidos,
    SUM(CASE WHEN e.status = 'atrasado' THEN 1 ELSE 0 END) AS atrasados
FROM Usuario u
INNER JOIN Emprestimo e ON u.id_usuario = e.id_usuario
GROUP BY u.id_usuario, u.tipo_usuario, u.nome, u.email
HAVING COUNT(e.id_emprestimo) >= 1
ORDER BY total_emprestimos DESC;

-- 6. itens mais emprestados
SELECT 
    ia.titulo,
    t.nome AS tipo,
    COUNT(e.id_emprestimo) AS vezes_emprestado,
    GROUP_CONCAT(DISTINCT loc.setor SEPARATOR ', ') AS setores,
    (SELECT COUNT(*) FROM Exemplar WHERE id_item_acervo = ia.id_item_acervo) AS qtd_exemplares
FROM ItemAcervo ia
INNER JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
INNER JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
INNER JOIN Emprestimo e ON ex.id_exemplar = e.id_exemplar
LEFT JOIN Localizacao loc ON ex.id_localizacao = loc.id_localizacao
GROUP BY ia.id_item_acervo, ia.titulo, t.nome
HAVING COUNT(e.id_emprestimo) >= 1
ORDER BY vezes_emprestado DESC
LIMIT 10;

-- 7. emprestimos atrasados com dados usuario e item
SELECT 
    u.nome AS usuario,
    u.email,
    u.tipo_usuario,
    ia.titulo AS item_emprestado,
    ex.codigo_barras,
    e.data_emprestimo,
    e.data_prevista_devolucao,
    DATEDIFF(CURRENT_DATE, e.data_prevista_devolucao) AS dias_atraso
FROM Emprestimo e
INNER JOIN Usuario u ON e.id_usuario = u.id_usuario
INNER JOIN Exemplar ex ON e.id_exemplar = ex.id_exemplar
INNER JOIN ItemAcervo ia ON ex.id_item_acervo = ia.id_item_acervo
WHERE e.status = 'atrasado'
   OR (e.data_devolucao IS NULL AND e.data_prevista_devolucao < CURRENT_DATE)
ORDER BY dias_atraso DESC;

-- 8. estatisticas acervo por tipo e assunto
SELECT 
    t.nome AS tipo_item,
    a.descricao AS assunto,
    COUNT(DISTINCT ia.id_item_acervo) AS qtd_itens,
    COUNT(DISTINCT ex.id_exemplar) AS qtd_exemplares,
    SUM(CASE WHEN ex.disponivel = TRUE THEN 1 ELSE 0 END) AS exemplares_disponiveis
FROM TipoItemAcervo t
INNER JOIN ItemAcervo ia ON t.id_tipo = ia.id_tipo
LEFT JOIN ItemAssunto ias ON ia.id_item_acervo = ias.id_item_acervo
LEFT JOIN Assunto a ON ias.id_assunto = a.id_assunto
LEFT JOIN Exemplar ex ON ia.id_item_acervo = ex.id_item_acervo
GROUP BY t.nome, a.descricao
HAVING COUNT(DISTINCT ia.id_item_acervo) >= 1
ORDER BY t.nome, qtd_itens DESC;

-- 9. livros por editora com contagem autores
SELECT 
    e.nome AS editora,
    ia.titulo,
    l.isbn,
    l.edicao,
    l.numero_paginas,
    COUNT(DISTINCT la.id_autor) AS qtd_autores,
    GROUP_CONCAT(
        DISTINCT CONCAT(a.nome, ' ', COALESCE(a.sobrenome, ''), ' (', la.papel, ')')
        SEPARATOR '; '
    ) AS autores
FROM Editora e
INNER JOIN Livro l ON e.id_editora = l.id_editora
INNER JOIN ItemAcervo ia ON l.id_livro = ia.id_item_acervo
LEFT JOIN LivroAutor la ON l.id_livro = la.id_livro
LEFT JOIN Autor a ON la.id_autor = a.id_autor
GROUP BY e.id_editora, e.nome, ia.id_item_acervo, ia.titulo, l.isbn, l.edicao, l.numero_paginas
HAVING COUNT(DISTINCT la.id_autor) >= 1
ORDER BY e.nome, ia.titulo;

-- 10. relatorio movimentacoes por periodo e tipo
SELECT 
    DATE_FORMAT(m.data_hora, '%Y-%m') AS mes_ano,
    m.tipo AS tipo_movimentacao,
    t.nome AS tipo_item,
    COUNT(*) AS total_movimentacoes,
    COUNT(DISTINCT m.id_usuario) AS usuarios_distintos,
    COUNT(DISTINCT m.id_item_acervo) AS itens_distintos
FROM Movimentacao m
LEFT JOIN ItemAcervo ia ON m.id_item_acervo = ia.id_item_acervo
LEFT JOIN TipoItemAcervo t ON ia.id_tipo = t.id_tipo
WHERE m.data_hora >= DATE_SUB(CURRENT_DATE, INTERVAL 12 MONTH)
GROUP BY DATE_FORMAT(m.data_hora, '%Y-%m'), m.tipo, t.nome
HAVING COUNT(*) >= 1
ORDER BY mes_ano DESC, total_movimentacoes DESC;
