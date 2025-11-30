# 📚 Sistema de Gestão da Biblioteca e do Acervo Histórico  
Museu Treze de Maio — Projeto Acadêmico

Este repositório contém a implementação completa dos dois sistemas solicitados no trabalho final da disciplina **Laboratório de Desenvolvimento de Software**:

- **Sistema de Gestão da Biblioteca Comunitária**
- **Sistema de Gestão do Acervo Histórico**

Ambos foram desenvolvidos em Java, utilizando JavaFX para interface gráfica e MySQL para persistência dos dados.

---

## 🎯 Objetivo do Projeto

Atender às necessidades do Museu Treze de Maio, permitindo:

- Catalogação dos itens da biblioteca (livros, revistas, jornais)
- Catalogação do acervo histórico (atas, cartas, fotos, relatos, objetos históricos)
- Consulta e gerenciamento por equipe interna, pesquisadores e comunidade

---

## 🏗 Arquitetura do Sistema

O projeto segue o padrão **MVC**, conforme recomendado no PDF:

- model/ → Classes de domínio
- controller/ → Lógica das telas
- view/ → Interfaces JavaFX (FXML)
- repositor/ → DAOs e acesso ao banco
- conexao/ → Classe de conexão MySQL

Tecnologias utilizadas:

- Java  
- JavaFX + Scene Builder  
- MySQL  
- JDBC  
- Maven  

---

## 📘 Funcionalidades — Biblioteca Comunitária

**Módulos implementados:**

- Cadastro de Itens de Acervo
- Cadastro de Livros
- Cadastro de Revistas
- Cadastro de Jornais
- Editoras
- Assuntos relacionados
- Exemplares e Localização
- Reservas
- Empréstimos

Inclui:

- CRUD completo  
- Validações  
- Tabelas com listagem  
- Movimentação de itens  

---

## 🗂 Funcionalidades — Acervo Histórico

**Módulos implementados:**

- Atas
- Cartas
- Fotografias
- Relatos orais
- Objetos históricos
- Jornais históricos

Todos vinculados à estrutura base **ItemAcervo**.

---

## 🗄 Banco de Dados

O banco segue o modelo proposto na disciplina, incluindo:

- ItemAcervo (classe base)
- Livro, Revista, Jornal
- Autor / LivroAutor
- Assunto / ItemAssunto
- Exemplar / Localização
- Empréstimo / Reserva
- Ata, Carta, Foto, Relato Oral, Objeto Histórico
- Movimentações
- TipoItemAcervo para classificação

O script SQL completo está em: biblioteca.sql


