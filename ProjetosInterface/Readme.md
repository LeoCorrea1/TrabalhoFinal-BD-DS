📚 Sistema de Gestão de Biblioteca e Acervo Histórico

Projeto desenvolvido para a disciplina Laboratório de Desenvolvimento de Software – 2025/2
Instituição: UFN – Universidade Franciscana

📌 Descrição do Projeto

Este projeto implementa dois sistemas independentes dentro de uma mesma aplicação Java:

🏛 1. Sistema de Biblioteca Comunitária

Permite gerenciar:

Livros

Revistas

Jornais

Item de Acervo (classe-base)

Exemplares

Usuários

Empréstimos

Reservas

Assuntos

Editoras

🗂 2. Sistema de Acervo Histórico (Museu)

Gerencia registros históricos:

Jornal Histórico

Ata

Carta

Fotografia

Relato Oral

Objeto Histórico

Item de Acervo (classe-base)

Ambos os sistemas utilizam uma estrutura comum chamada ItemAcervo, mas a interface e as funcionalidades são separadas, conforme exigido pela atividade.

🧱 Arquitetura Utilizada

A aplicação segue o padrão MVC:

src/
 └── main/java/com/example/projetofxcombd/
        ├── model/        → Classes de domínio (Livro, ItemAcervo, etc.)
        ├── controller/   → Controllers JavaFX
        ├── repositor/    → DAOs (ItemAcervoDAO, LivroDAO...)
        ├── conexao/      → Classe Db (conexão MySQL)
        ├── MainApplication.java
 └── main/resources/
        └── fxml/         → Telas JavaFX criadas no Scene Builder

🗄 Banco de Dados

O banco utilizado é MySQL.
Há um script SQL completo com:

Tabelas de Biblioteca

Tabelas de Acervo Histórico

Restrições, chaves estrangeiras

Classificação por TipoItemAcervo

Relacionamentos n:m (Livro ↔ Autor / Item ↔ Assunto)

Arquivo disponível em:

/database/biblioteca.sql
