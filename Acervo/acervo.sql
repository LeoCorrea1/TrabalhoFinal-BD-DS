-- Script para criar a tabela de acervo (MySQL)
CREATE TABLE IF NOT EXISTS acervo (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nome VARCHAR(255) NOT NULL,
  descricao TEXT,
  categoria VARCHAR(100),
  data_item DATE,
  tipo VARCHAR(50),
  responsavel VARCHAR(150),
  arquivo_path VARCHAR(300),
  status VARCHAR(20) DEFAULT 'ativo'
);

-- Exemplo de insert
INSERT INTO acervo (nome, descricao, categoria, data_item, tipo, responsavel, arquivo_path, status)
VALUES ('Fotografia - Greve de 1917', 'Foto histórica da greve', 'História', '1917-05-13', 'Foto', 'Arquivo Municipal', '', 'ativo');
