CREATE DATABASE BibliotecaDB;
USE BibliotecaDB;

CREATE TABLE ItemAcervo (
    id_item_acervo INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    subtitulo VARCHAR(255),
    ano INT,
    idioma VARCHAR(50),
    descricao TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
);

CREATE TABLE Editora (
    id_editora INT AUTO_INCREMENT PRIMARY KEY,
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
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    sobrenome VARCHAR(150),
    nacionalidade VARCHAR(100)
);

CREATE TABLE LivroAutor (
    id_livro_autor INT AUTO_INCREMENT PRIMARY KEY,
    id_livro INT NOT NULL,
    id_autor INT NOT NULL,
    papel VARCHAR(50),

    CONSTRAINT FK_LivroAutor_Livro FOREIGN KEY (id_livro)
        REFERENCES Livro(id_livro),

    CONSTRAINT FK_LivroAutor_Autor FOREIGN KEY (id_autor)
        REFERENCES Autor(id_autor)
);

CREATE TABLE Assunto (
    id_assunto INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(200) NOT NULL
);

CREATE TABLE ItemAssunto (
    id_item_assunto INT AUTO_INCREMENT PRIMARY KEY,
    id_item_acervo INT NOT NULL,
    id_assunto INT NOT NULL,

    CONSTRAINT FK_ItemAssunto_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_ItemAssunto_Assunto FOREIGN KEY (id_assunto)
        REFERENCES Assunto(id_assunto)
);

CREATE TABLE Localizacao (
    id_localizacao INT AUTO_INCREMENT PRIMARY KEY,
    setor VARCHAR(100),
    prateleira VARCHAR(100),
    caixa VARCHAR(100)
);

CREATE TABLE Exemplar (
    id_exemplar INT AUTO_INCREMENT PRIMARY KEY,
    id_item_acervo INT NOT NULL,
    codigo_barras VARCHAR(100) NOT NULL UNIQUE,
    estado_conservacao VARCHAR(50),
    disponivel BOOLEAN DEFAULT TRUE,
    id_localizacao INT,

    CONSTRAINT FK_Exemplar_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_Exemplar_Local FOREIGN KEY (id_localizacao)
        REFERENCES Localizacao(id_localizacao)
);

CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(50),
    tipo_usuario VARCHAR(20),
    ativo BOOLEAN DEFAULT TRUE,
    CHECK (tipo_usuario IN ('aluno','professor','publico','tecnico'))
);

CREATE TABLE Emprestimo (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_exemplar INT NOT NULL,
    id_usuario INT NOT NULL,
    data_emprestimo DATETIME NOT NULL,
    data_prevista_devolucao DATETIME NOT NULL,
    data_devolucao DATETIME,
    status VARCHAR(20),
    CHECK (status IN ('ativo','devolvido','atrasado','perdido')),

    CONSTRAINT FK_Emprestimo_Exemplar FOREIGN KEY (id_exemplar)
        REFERENCES Exemplar(id_exemplar),

    CONSTRAINT FK_Emprestimo_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);

CREATE TABLE Reserva (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
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
    id_mov INT AUTO_INCREMENT PRIMARY KEY,
    id_item_acervo INT,
    id_exemplar INT,
    id_usuario INT,
    tipo VARCHAR(50),
    descricao TEXT,
    data_hora DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT FK_Mov_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo),

    CONSTRAINT FK_Mov_Exemplar FOREIGN KEY (id_exemplar)
        REFERENCES Exemplar(id_exemplar),

    CONSTRAINT FK_Mov_Usuario FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);

CREATE TABLE TipoItemAcervo (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

ALTER TABLE ItemAcervo
ADD id_tipo INT;

ALTER TABLE ItemAcervo
ADD CONSTRAINT FK_ItemAcervo_Tipo
    FOREIGN KEY (id_tipo)
    REFERENCES TipoItemAcervo(id_tipo);
    
CREATE TABLE Jornal (
    id_item_acervo INT PRIMARY KEY,
    edicao VARCHAR(50),
    data_publicacao DATE,
    periodicidade VARCHAR(50),

    CONSTRAINT FK_Jornal_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);

CREATE TABLE Ata (
    id_item_acervo INT PRIMARY KEY,
    orgao_responsavel VARCHAR(200),
    data_reuniao DATE,
    participantes TEXT,

    CONSTRAINT FK_Ata_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);

CREATE TABLE Fotografia (
    id_item_acervo INT PRIMARY KEY,
    fotografo VARCHAR(255),
    ano_registro INT,
    formato VARCHAR(50),
    local VARCHAR(255),

    CONSTRAINT FK_Foto_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);

CREATE TABLE Carta (
    id_item_acervo INT PRIMARY KEY,
    remetente VARCHAR(255),
    destinatario VARCHAR(255),
    data_carta DATE,
    local_emissao VARCHAR(255),

    CONSTRAINT FK_Carta_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);

CREATE TABLE RelatoOral (
    id_item_acervo INT PRIMARY KEY,
    entrevistado VARCHAR(255),
    entrevistador VARCHAR(255),
    data_entrevista DATE,
    duracao VARCHAR(50),
    formato_arquivo VARCHAR(50),

    CONSTRAINT FK_Relato_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);

CREATE TABLE ObjetoHistorico (
    id_item_acervo INT PRIMARY KEY,
    material VARCHAR(200),
    dimensoes VARCHAR(200),
    estado_conservacao VARCHAR(200),
    procedencia VARCHAR(255),

    CONSTRAINT FK_Objeto_Item FOREIGN KEY (id_item_acervo)
        REFERENCES ItemAcervo(id_item_acervo)
);
