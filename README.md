#  Sistema da Biblioteca e do Acervo Histórico  
Museu Treze de Maio 

Este documento apresenta um resumo dos dois sistemas desenvolvidos para o Museu Treze de Maio:

- **Sistema da Biblioteca Comunitária**
- **Sistema do Acervo Histórico**

Feitos em **Java + JavaFX**, com banco **MySQL**.

---

##  Objetivo

Fornecer um sistema simples e local para:

**Sistema da Biblioteca Comunitária**
-  Gerenciar itens da biblioteca (itensAcervo)
- Controlar usuários, exemplares, empréstimos, reservas e movimentações

 **Sistema do Acervo Histórico**
- CRUD de Tipos de Item (TipoItemAcervo), classificação dos itens por tipo histórico, e vínculo de Autores a Livros (tabela LivroAutor).
-  A interface inclui uma galeria/listagem de itens e uma tela de detalhe que exibe metadados.


---

##  Arquitetura

- `model/` — classes de domínio  
- `controller/` — lógica das telas  
- `fxml/` — FXML + JavaFX  
- `dao/` — DAOs  
- `conexao/` — conexão MySQL  

Tecnologias: **Java, JavaFX, MySQL, JDBC, SceneBuilder , Maven**

---

##  Biblioteca — Funcionalidades

- Itens de Acervo  
- Editoras  
- Exemplares + Localização  
- Reservas  
- Empréstimos
- Usuarios
- Visualizaçao de Movimentaçoes

Inclui CRUD completo, validação e listagens.

---

##  Acervo Histórico — Funcionalidades

- Galeria do Acervo (visualização detalhada)
- Tipos de Itens Acervo
- Autores
- Vinculação Autor ↔ Livro

Inclui CRUD completo de Itens, filtros e listagens..

---

##  Banco de Dados

Principais entidades:

- ItemAcervo  
- Livro / Revista / Jornal  
- Exemplar / Localização  
- Empréstimo / Reserva  
- Autor / Assunto  
- Ata / Carta / Foto / Relato / Objeto  
- Movimentações  
- TipoItemAcervo  

---

