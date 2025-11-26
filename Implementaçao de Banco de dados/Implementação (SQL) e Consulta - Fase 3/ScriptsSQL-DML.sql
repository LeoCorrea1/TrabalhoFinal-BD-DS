
-- EDITORAS
INSERT INTO Editora (nome, contato, endereco) VALUES
('Editora Atlas', 'contato@atlas.com', 'SP'),
('Companhia das Letras', 'contato@cl.com', 'SP'),
('Saraiva', 'info@saraiva.com', 'RJ');

-- ITENS DO ACERVO / LIVROS
INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao)
VALUES
('Introdu  o   Programa  o', 'L gica e Algoritmos', 2020, 'Portugu s', 'Livro t cnico'),
('Banco de Dados Moderno', 'Teoria e Pr tica', 2021, 'Portugu s', 'Modelagem e SQL'),
('Redes de Computadores', 'Conceitos B sicos', 2018, 'Portugu s', 'TCP/IP'),
('Intelig ncia Artificial', 'Fundamentos', 2022, 'Ingl s', 'Aprendizado de m quina'),
('Engenharia de Software', 'Pr ticas  geis', 2019, 'Portugu s', 'Agile e Scrum'),
('Python para Iniciantes', NULL, 2020, 'Portugu s', 'Introdu  o ao Python'),
('Estruturas de Dados', NULL, 2017, 'Portugu s', 'Listas,  rvores e grafos'),
('Algoritmos Avan ados', NULL, 2021, 'Ingl s', 'Complexidade'),
('Matem tica Discreta', NULL, 2018, 'Portugu s', 'Conceitos b sicos'),
('Machine Learning', NULL, 2022, 'Ingl s', 'Modelos modernos');

-- LIVROS (1:1 com ItemAcervo)
INSERT INTO Livro (id_livro, isbn, edicao, numero_paginas, id_editora)
VALUES
(1,'978-1', '1', 350, 1),
(2,'978-2', '2', 420, 2),
(3,'978-3', '7', 500, 3),
(4,'978-4', '1', 310, 1),
(5,'978-5', '3', 450, 2),
(6,'978-6', '1', 280, 3),
(7,'978-7', '4', 300, 2),
(8,'978-8', '1', 290, 1),
(9,'978-9', '2', 410, 3),
(10,'978-10','1', 380, 1);

-- AUTORES
INSERT INTO Autor (nome, sobrenome, nacionalidade) VALUES
('Jo o','Silva','Brasileiro'),
('Carlos','Pereira','Brasileiro'),
('Ana','Souza','Brasileira'),
('John','Miller','Americano'),
('Maria','Oliveira','Brasileira'),
('Lucas','Dias','Brasileiro'),
('Roberto','Santos','Brasileiro'),
('Emily','Clark','Inglesa'),
('Paulo','Rios','Brasileiro'),
('Amanda','Torres','Brasileira');

-- RELA  O LIVRO-AUTOR
INSERT INTO LivroAutor (id_livro, id_autor, papel) VALUES
(1,1,'autor'), (1,2,'coautor'),
(2,3,'autor'),
(3,4,'autor'),
(4,5,'autor'),
(5,6,'autor'),
(6,7,'autor'),
(7,8,'autor'),
(8,9,'autor'),
(9,10,'autor'),
(10,1,'autor');

-- ASSUNTOS
INSERT INTO Assunto (descricao) VALUES
('Tecnologia'), ('Programa  o'), ('IA'), ('Redes'), ('Banco de Dados');

-- ITEM_ASSUNTO
INSERT INTO ItemAssunto (id_item_acervo, id_assunto) VALUES
(1,2),(2,5),(3,4),(4,3),(5,1),
(6,2),(7,2),(8,2),(9,1),(10,3);

-- LOCALIZA  O
INSERT INTO Localizacao (setor, prateleira, caixa) VALUES
('TI','A1','1'), ('TI','A2','1'), ('TI','A3','2');

-- EXEMPLARES
INSERT INTO Exemplar (id_item_acervo, codigo_barras, estado_conservacao, disponivel, id_localizacao)
VALUES
(1,'1001','bom',1,1),
(2,'1002','bom',1,1),
(3,'1003','regular',1,2),
(4,'1004','bom',1,2),
(5,'1005','ruim',1,3),
(6,'1006','bom',1,1),
(7,'1007','bom',1,3),
(8,'1008','bom',1,2),
(9,'1009','regular',1,1),
(10,'1010','bom',1,3);

-- USUARIOS
INSERT INTO Usuario (nome,email,telefone,tipo_usuario)
VALUES
('Leonardo','leo@mail.com','9999','aluno'),
('Carla','carla@mail.com','8888','professor'),
('Jo o','joao@mail.com','7777','aluno'),
('Marina','marina@mail.com','6666','publico');

-- EMPRESTIMOS
INSERT INTO Emprestimo (id_exemplar, id_usuario, data_emprestimo, data_prevista_devolucao, status)
VALUES
(1,1,GETDATE(),'2024-12-20','ativo'),
(2,2,GETDATE(),'2024-12-18','devolvido'),
(3,1,GETDATE(),'2024-12-22','atrasado');

-- RESERVAS
INSERT INTO Reserva (id_exemplar, id_usuario, data_reserva, data_expiracao, status)
VALUES
(5,3,GETDATE(),'2024-12-25','ativa'),
(7,1,GETDATE(),'2024-12-23','ativa');
