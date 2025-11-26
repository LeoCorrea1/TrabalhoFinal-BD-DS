
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

