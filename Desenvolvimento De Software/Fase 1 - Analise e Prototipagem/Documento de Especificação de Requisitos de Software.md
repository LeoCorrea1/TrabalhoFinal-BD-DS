# ERS -- Sistema de Gestão do Acervo e Biblioteca (JavaFX)

## Versão

1.1 -- Adaptada ao sistema atual

## Plataforma

Java 17 + JavaFX + Scene Builder + Maven

## Data

01/12/2025

------------------------------------------------------------------------

# 1. Introdução

## 1.1 Propósito

Este documento especifica os requisitos do sistema desktop JavaFX
utilizado pelo Museu Treze de Maio para gerenciar: - Acervo da
biblioteca - Usuários - Exemplares - Empréstimos e devoluções

## 1.2 Escopo

O sistema é local e offline, com telas JavaFX e conexão ao banco SQL
Server. Inclui: - CRUD de usuários - CRUD de livros - CRUD de
exemplares - Controle de empréstimos

------------------------------------------------------------------------

# 2. Descrição Geral

## 2.1 Perspectiva do Produto

Software desktop executado em Windows, com formulários, tabelas JavaFX e
integração com banco relacional.

## 2.2 Funcionalidades Existentes

-   Cadastro de usuários
-   Cadastro de livros
-   Cadastro de exemplares
-   Empréstimos
-   Devoluções
-   Pesquisas simples

## 2.3 Usuários

-   Administrador
-   Bibliotecário
-   Visitante (consulta, opcional)

## 2.4 Restrições

-   JavaFX obrigatório
-   Banco SQL Server atual
-   Execução offline
-   Ambiente Windows

------------------------------------------------------------------------

# 3. Requisitos Específicos

## 3.1 Requisitos Funcionais (RF)

### RF001 -- Cadastro de Usuários

Permitir cadastrar, editar, excluir e listar usuários.

### RF002 -- Cadastro de Livros

Registrar título, autor, categoria, ano, ISBN, editora.

### RF003 -- Cadastro de Exemplares

Vincular a um livro, registrar estado e disponibilidade.

### RF004 -- Empréstimos

Selecionar usuário + exemplar e registrar datas.

### RF005 -- Devoluções

Marcar empréstimo como devolvido e atualizar disponibilidade.

### RF006 -- Pesquisa

Buscar livros, usuários, empréstimos e exemplares.

### RF007 -- Login (Opcional)

Sistema simples com senha armazenada no banco.

### RF008 -- Logs (Opcional)

Registrar alterações realizadas.

------------------------------------------------------------------------

## 3.2 Requisitos Não Funcionais (RNF)

### RNF001 -- Usabilidade

Interface clara com JavaFX e Scene Builder.

### RNF002 -- Desempenho

Abertura da aplicação em até 5--10 segundos.

### RNF003 -- Armazenamento

Tabelas: Usuario, Livro, Exemplar, Emprestimo.

### RNF004 -- Segurança

Senhas com hash caso login seja usado.

### RNF005 -- Confiabilidade

Operação totalmente offline.

### RNF006 -- Portabilidade

Compatível com Windows 10+.

------------------------------------------------------------------------

# 4. Modelagem do Sistema

## 4.1 Entidades

-   Usuario(id, nome, email, telefone)
-   Livro(id, titulo, autor, categoria, isbn, editora, ano)
-   Exemplar(id, idLivro, estado, disponibilidade)
-   Emprestimo(id, idExemplar, idUsuario, datas, status)

## 4.2 Relacionamentos

-   Livro 1:N Exemplar\
-   Exemplar 1:N Empréstimo\
-   Usuario 1:N Empréstimo

------------------------------------------------------------------------

# 5. Interfaces

## 5.1 Telas

-   Login (opcional)
-   Menu Principal
-   Usuários
-   Livros
-   Exemplares
-   Empréstimos
-   Devoluções
-   Pesquisa/Listagem

------------------------------------------------------------------------

# 6. Critérios de Aceitação

-   Todos os CRUDs funcionando.
-   Empréstimos atualizam disponibilidade.
-   Devoluções registradas corretamente.
-   Tabelas JavaFX exibem dados corretamente.
-   Banco conectado e persistindo dados.
-   Projeto compila via Maven.

------------------------------------------------------------------------

# 7. Anexos

-   Script SQL atual
-   Estrutura de pacotes Java
-   Protótipos de telas (opcional)
