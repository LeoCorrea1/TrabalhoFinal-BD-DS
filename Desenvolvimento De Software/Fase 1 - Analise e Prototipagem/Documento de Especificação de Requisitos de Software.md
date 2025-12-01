# ERS -- Sistema de Gestão do Acervo e Biblioteca (JavaFX)

## Plataforma

IntelliJ - Java 17 + JavaFX + Scene Builder + Maven

------------------------------------------------------------------------

# 1. Introdução

## 1.1 Propósito

Este documento especifica os requisitos do sistema desktop JavaFX
que será Utiliazdo pelo Museu Treze de Maio para gerenciar: 
- Acervo da biblioteca - Usuários - Exemplares - Empréstimos - Movimentaçoes e devoluções

## 1.2 Escopo

O sistema é local e offline, com telas JavaFX e conexão ao banco MySQL
Inclui: - CRUD de usuários - CRUD de itensAcervo - CRUD de
exemplares - Controle de empréstimos e Muito mais

------------------------------------------------------------------------

# 2. Descrição Geral

## 2.1 Perspectiva do Produto

Software desktop executado em Windows, com formulários, tabelas JavaFX e
integração com banco de dados ( De acordo com as especificaçoes da disciplina Implementaçao de banco de dados )

## 2.2 Funcionalidades Existentes

-   Cadastro de usuários
-   Cadastro de Itens no Acervo , Editoras ( Gestao Total )
-   Cadastro de exemplares
-   Empréstimos
-   Devoluções
-   Reservas
-   Pesquisas simples ( itensAcervo, Editoras, Exemplares de um ItemSelecionado e etc )

## 2.3 Usuários

-   Tecnico
-   publico
-   Professor
-   Funcionario

## 2.4 Restrições

-   JavaFX obrigatório
-   Banco MySQL Atual
-   Ambiente Windows

------------------------------------------------------------------------

# 3. Requisitos Específicos

## 3.1 Requisitos Funcionais (RF)

### RF001 -- Cadastro de Usuários

Permitir cadastrar, editar, excluir e listar usuários.

### RF002 -- Cadastro de Itens do Acervo

Registrar título, tipoAcervo , categoria, ano, ISBN, editora e etc.

### RF003 -- Cadastro de Exemplares

Vincular a um livro ( itemAcervo ), registrar estado e disponibilidade.

### RF004 -- Empréstimos

Selecionar usuário + exemplar e registrar datas.

### RF005 -- Devoluções

Marcar empréstimo como devolvido e atualizar disponibilidade.

### RF006 -- Pesquisa

Buscar ItensAcervo,  usuários, empréstimos, Reservas e exemplares.

### RF007 -- Logs 

Registrar alterações realizadas ( Movimentaçoes ).

------------------------------------------------------------------------

## 3.2 Requisitos Não Funcionais (RNF)

### RNF001 -- Usabilidade

Interface clara com JavaFX e Scene Builder.

### RNF002 -- Desempenho

Abertura da aplicação em até 2--5 segundos.

### RNF003 -- Armazenamento

Tabelas: Usuario, itenAcervo, Exemplar, Emprestimo , Reserva , Editora e Movimentaçoes ... .

### RNF004 -- Portabilidade

Compatível com Windows 10+.

------------------------------------------------------------------------

# 4. Modelagem do Sistema

## 4.1 Entidades (principais do sistema)

-   ItemAcervo(id_item_acervo, titulo, subtitulo, ano, idioma, descricao, id_tipo)
-   TipoItemAcervo(id_tipo, nome)
-   Editora(id_editora, nome, contato)
-   Livro(id_livro = ItemAcervo.id_item_acervo, isbn, edicao, numero_paginas, id_editora)
-   Localizacao(id_localizacao, setor, prateleira, caixa)
-   Exemplar(id_exemplar, id_item_acervo, codigo_barras, estado_conservacao, disponivel, id_localizacao)
-   Usuario(id_usuario, nome, email, telefone, tipo_usuario, ativo)
-   Emprestimo(id_emprestimo, id_exemplar, id_usuario, datas, status)
-   Reserva(id_reserva, id_exemplar, id_usuario, data_reserva, data_expiracao, status)
-   Movimentacao(id_mov, id_item_acervo, id_exemplar, id_usuario, tipo, descricao, data_hora)

## 4.2 Relacionamentos

-   ItemAcervo 1:N Exemplar
-   ItemAcervo 1:N ItemAssunto
-   ItemAcervo 1:1 Livro (quando o item é um livro)
-   Exemplar 1:N Emprestimo
-   Exemplar 1:N Reserva
-   Usuario 1:N Emprestimo
-   Usuario 1:N Reserva
-   Usuario 1:N Movimentacao ( Para Log )
-   Localizacao 1:N Exemplar
  
------------------------------------------------------------------------

# 5. Interfaces

## 5.1 Telas

-   DashBoard
-   ItensAcervo
-   Exemplares
-   Empréstimos
-   Devoluções
-   Movimentaçoes
-   Usuários
-   Editoras
-   E muito mais...

------------------------------------------------------------------------

# 6. Critérios de Aceitação

-   Todos os CRUDs funcionando.
-   Empréstimos registrados corretamente.
-   Devoluções registradas corretamente.
-   Tabelas JavaFX exibem dados corretamente.
-   Banco conectado e persistindo dados.
-   Projeto compila via Maven.

------------------------------------------------------------------------
