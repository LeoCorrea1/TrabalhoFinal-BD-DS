📚 Sistema de Gestão da Biblioteca e do Acervo Histórico

Este documento descreve, de forma resumida, os principais requisitos do sistema desktop desenvolvido para o Museu Treze de Maio, integrando tanto a Biblioteca Comunitária quanto o Acervo Histórico.
O sistema foi construído em JavaFX, com persistência em MySQL, seguindo o padrão MVC utilizado no projeto.

1. Introdução
1.1 Propósito

Registrar e gerenciar todos os itens do acervo (biblioteca + histórico), além de usuários, exemplares, empréstimos, reservas e movimentações.

1.2 Escopo

Aplicação local e offline, com telas JavaFX e CRUD completo para:

Biblioteca (livros, revistas, jornais)

Acervo histórico (atas, cartas, fotos, relatos, objetos históricos)

Usuários, exemplares, empréstimos, reservas e editoras

2. Descrição Geral
2.1 Perspectiva

Software desktop para Windows, com formulários e tabelas JavaFX, integrado ao banco definido na disciplina.

2.2 Funcionalidades

Cadastro de usuários

Cadastro geral de ItemAcervo e especializações

Editoras, assuntos, autores

Exemplares e localizações

Empréstimos, devoluções e reservas

Movimentações (log)

Pesquisas simples em todas as entidades

2.3 Usuários

Técnico, Funcionário, Professor e Público.

2.4 Restrições

JavaFX, MySQL e ambiente Windows.
