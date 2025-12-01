-- TipoItemAcervoo
INSERT INTO TipoItemAcervo (nome, descricao) VALUES
('Livro', 'Publicações impressas'),
('Jornal', 'Publicações periódicas'),
('Ata', 'Registros de reuniões'),
('Fotografia', 'Imagens fotográficas'),
('Carta', 'Correspondências'),
('Relato Oral', 'Entrevistas gravadas'),
('Objeto Histórico', 'Objetos antigos');

-- Editora
INSERT INTO Editora (nome, contato, endereco) VALUES
('Editora Atlas', 'contato@atlas.com', 'Rua Central, 120 - São Paulo'),
('Companhia das Letras', 'contato@ciasletras.com', 'Av. Paulista, 1500 - São Paulo'),
('Editora Moderna', 'suporte@moderna.com', 'Rua da Educação, 85 - Rio de Janeiro');

-- ItemAcervo
INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao, id_tipo)
VALUES
('Introdução à Programação', 'Princípios básicos', 2020, 'Português', 'Livro básico para iniciantes', 1),
('O Estado de São Paulo', 'Edição de domingo', 2023, 'Português', 'Jornal diário', 2),
('Ata da Reunião do Conselho', NULL, 2022, 'Português', 'Discussões administrativas', 3),
('Foto da Inauguração da Biblioteca', NULL, 1985, 'Sem idioma', 'Fotografia histórica', 4),
('Carta de Getúlio Vargas', 'A um aliado político', 1937, 'Português', 'Correspondência histórica', 5),
('Entrevista com João Silva', NULL, 1999, 'Português', 'Relato oral gravado', 6),
('Vaso Indígena', NULL, 1890, 'N/A', 'Artefato histórico preservado', 7);

-- Livro
INSERT INTO Livro (id_livro, isbn, edicao, numero_paginas, id_editora)
VALUES
(1, '978-8535701234', '2ª edição', 350, 1);

-- Autor
INSERT INTO Autor (nome, sobrenome, nacionalidade) VALUES
('Carlos', 'Silva', 'Brasileiro'),
('Mariana', 'Souza', 'Brasileira'),
('John', 'Doe', 'Americano');

-- LivroAutor
INSERT INTO LivroAutor (id_livro, id_autor, papel) VALUES
(1, 1, 'Autor'),
(1, 2, 'Coautor');

-- Assunto
INSERT INTO Assunto (descricao) VALUES
('Programação'),
('Tecnologia'),
('História'),
('Política'),
('Educação');

-- ItemAssunto
INSERT INTO ItemAssunto (id_item_acervo, id_assunto) VALUES
(1, 1),
(1, 2),
(5, 4),
(7, 3);

-- Localizacao
INSERT INTO Localizacao (setor, prateleira, caixa) VALUES
('Tecnologia', 'T1', 'Caixa 12'),
('História', 'H2', 'Caixa 5'),
('Arquivo Geral', 'AG1', 'Caixa 20');

-- Exemplar
INSERT INTO Exemplar (id_item_acervo, codigo_barras, estado_conservacao, disponivel, id_localizacao)
VALUES
(1, 'LIV001-A', 'Bom', TRUE, 1),
(1, 'LIV001-B', 'Regular', TRUE, 1),
(4, 'FOTO001', 'Ótimo', TRUE, 2),
(7, 'OBJ001', 'Frágil', FALSE, 3);

-- Usuario
INSERT INTO Usuario (nome, email, telefone, tipo_usuario) VALUES
('Ana Pereira', 'ana.p@gmail.com', '11999990000', 'aluno'),
('João Mendes', 'joao.mendes@hotmail.com', '21988880000', 'professor'),
('Marcos Lima', 'marcos.lima@gmail.com', '31977770000', 'publico');

-- Emprestimo
INSERT INTO Emprestimo
(id_exemplar, id_usuario, data_emprestimo, data_prevista_devolucao, status)
VALUES
(1, 1, '2025-02-01 10:00:00', '2025-02-15 23:59:59', 'ativo');

-- Reserva
INSERT INTO Reserva (id_exemplar, id_usuario, data_reserva, data_expiracao, status)
VALUES
(2, 1, '2025-02-05 14:00:00', '2025-02-07 14:00:00', 'ativa');

-- Movimentacao
INSERT INTO Movimentacao (id_item_acervo, id_exemplar, id_usuario, tipo, descricao)
VALUES
(1, 1, 1, 'emprestimo', 'Empréstimo realizado'),
(4, 3, 3, 'consulta', 'Consulta de fotografia');

