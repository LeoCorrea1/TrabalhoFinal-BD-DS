
CREATE DATABASE BibliotecaDB;
GO
USE BibliotecaDB;
GO

CREATE TABLE ItemAcervo (
    id_item_acervo INT IDENTITY PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    subtitulo VARCHAR(255),
    ano INT,
    idioma VARCHAR(50),
    descricao TEXT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME
);

CREATE TABLE Editora (
    id_editora INT IDENTITY PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    contato VARCHAR(200),
    endereco VARCHAR(300)
);

CREATE TABLE Livro (
    id_livro INT PRIMARY KEY,
    isbn VARCHAR(20),
    edicao VARCHAR(20),
    numero_paginas INT,
    id_editora INT NOT NULL,

    CONSTRAINT FK_Livro_ItemAcervo FOREIGN KEY (id_livro)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_Livro_Editora FOREIGN KEY (id_editora)
        REFERENCES Editora(id_editora),

    CONSTRAINT UQ_Livro_ISBN UNIQUE (isbn)
);

CREATE TABLE Autor (
    id_autor INT IDENTITY PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    sobrenome VARCHAR(150),
    nacionalidade VARCHAR(100)
);

CREATE TABLE LivroAutor (
    id_livro_autor INT IDENTITY PRIMARY KEY,
    id_livro INT NOT NULL,
    id_autor INT NOT NULL,
    papel VARCHAR(50),

    CONSTRAINT FK_LivroAutor_Livro FOREIGN KEY (id_livro)
        REFERENCES Livro(id_livro),

    CONSTRAINT FK_LivroAutor_Autor FOREIGN KEY (id_autor)
        REFERENCES Autor(id_autor)
);

CREATE TABLE Assunto (
    id_assunto INT IDENTITY PRIMARY KEY,
    descricao VARCHAR(200) NOT NULL
);

CREATE TABLE ItemAssunto (
    id_item_assunto INT IDENTITY PRIMARY KEY,
    id_item_acervo INT NOT NULL,
    id_assunto INT NOT NULL,

    CONSTRAINT FK_ItemAssunto_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_ItemAssunto_Assunto FOREIGN KEY (id_assunto)
        REFERENCES Assunto(id_assunto)
);

CREATE TABLE Localizacao (
    id_localizacao INT IDENTITY PRIMARY KEY,
    setor VARCHAR(100),
    prateleira VARCHAR(100),
    caixa VARCHAR(100)
);

CREATE TABLE Exemplar (
    id_exemplar INT IDENTITY PRIMARY KEY,
    id_item_acervo INT NOT NULL,
    codigo_barras VARCHAR(100) NOT NULL UNIQUE,
    estado_conservacao VARCHAR(50),
    disponivel BIT DEFAULT 1,
    id_localizacao INT,

    CONSTRAINT FK_Exemplar_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_Exemplar_Local FOREIGN KEY (id_localizacao)
        REFERENCES Localizacao(id_localizacao)
);

CREATE TABLE Usuario (
    id_usuario INT IDENTITY PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(50),
    tipo_usuario VARCHAR(20) CHECK (tipo_usuario IN ('aluno','professor','publico','tecnico')),
    ativo BIT DEFAULT 1
);

CREATE TABLE Emprestimo (
    id_emprestimo INT IDENTITY PRIMARY KEY,
    id_exemplar INT NOT NULL,
    id_usuario INT NOT NULL,
    data_emprestimo DATETIME NOT NULL,
    data_prevista_devolucao DATETIME NOT NULL,
    data_devolucao DATETIME,
    status VARCHAR(20) CHECK (status IN ('ativo','devolvido','atrasado','perdido')),

    CONSTRAINT FK_Emprestimo_Exemplar FOREIGN KEY (id_exemplar)
        REFERENCES Exemplar(id_exemplar),

    CONSTRAINT FK_Emprestimo_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);

CREATE TABLE Reserva (
    id_reserva INT IDENTITY PRIMARY KEY,
    id_exemplar INT NOT NULL,
    id_usuario INT NOT NULL,
    data_reserva DATETIME NOT NULL,
    data_expiracao DATETIME NOT NULL,
    status VARCHAR(20),

    CONSTRAINT FK_Reserva_Exemplar FOREIGN KEY (id_exemplar)
        REFERENCES Exemplar(id_exemplar),

    CONSTRAINT FK_Reserva_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);

CREATE TABLE Movimentacao (
    id_mov INT IDENTITY PRIMARY KEY,
    id_item_acervo INT,
    id_exemplar INT,
    id_usuario INT,
    tipo VARCHAR(50),
    descricao TEXT,
    data_hora DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Mov_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_Mov_Exemplar FOREIGN KEY (id_exemplar)
        REFERENCES Exemplar(id_exemplar),

    CONSTRAINT FK_Mov_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);


-- EDITORAS
INSERT INTO Editora (nome, contato, endereco) VALUES
('Editora Atlas', 'contato@atlas.com', 'SP'),
('Companhia das Letras', 'contato@cl.com', 'SP'),
('Saraiva', 'info@saraiva.com', 'RJ');

-- ITENS DO ACERVO / LIVROS
INSERT INTO ItemAcervo (titulo, subtitulo, ano, idioma, descricao)
VALUES
('Introdução à Programação', 'Lógica e Algoritmos', 2020, 'Português', 'Livro técnico'),
('Banco de Dados Moderno', 'Teoria e Prática', 2021, 'Português', 'Modelagem e SQL'),
('Redes de Computadores', 'Conceitos Básicos', 2018, 'Português', 'TCP/IP'),
('Inteligência Artificial', 'Fundamentos', 2022, 'Inglês', 'Aprendizado de máquina'),
('Engenharia de Software', 'Práticas Ágeis', 2019, 'Português', 'Agile e Scrum'),
('Python para Iniciantes', NULL, 2020, 'Português', 'Introdução ao Python'),
('Estruturas de Dados', NULL, 2017, 'Português', 'Listas, árvores e grafos'),
('Algoritmos Avançados', NULL, 2021, 'Inglês', 'Complexidade'),
('Matemática Discreta', NULL, 2018, 'Português', 'Conceitos básicos'),
('Machine Learning', NULL, 2022, 'Inglês', 'Modelos modernos');

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
('João','Silva','Brasileiro'),
('Carlos','Pereira','Brasileiro'),
('Ana','Souza','Brasileira'),
('John','Miller','Americano'),
('Maria','Oliveira','Brasileira'),
('Lucas','Dias','Brasileiro'),
('Roberto','Santos','Brasileiro'),
('Emily','Clark','Inglesa'),
('Paulo','Rios','Brasileiro'),
('Amanda','Torres','Brasileira');

-- RELAÇÃO LIVRO-AUTOR
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
('Tecnologia'), ('Programação'), ('IA'), ('Redes'), ('Banco de Dados');

-- ITEM_ASSUNTO
INSERT INTO ItemAssunto (id_item_acervo, id_assunto) VALUES
(1,2),(2,5),(3,4),(4,3),(5,1),
(6,2),(7,2),(8,2),(9,1),(10,3);

-- LOCALIZAÇÃO
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
('João','joao@mail.com','7777','aluno'),
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
