Módulo Acervo - scaffold
------------------------

Conteúdo:
- src/main/java/com/museu/acervo/model/Acervo.java
- src/main/java/com/museu/acervo/dao/ConnectionFactory.java
- src/main/java/com/museu/acervo/dao/AcervoDAO.java
- src/main/java/com/museu/acervo/controller/AcervoController.java
- src/main/resources/fxml/acervo.fxml
- sql/create_acervo.sql
- pom.xml

Como usar:
1. Ajuste as credenciais no ConnectionFactory.java.
2. Rode o script SQL (sql/create_acervo.sql) no banco.
3. Importe o projeto em sua IDE como Maven (ou incorpore os fontes ao projeto principal).
4. Ajuste mainClass / integração com o seu app principal. O controller e o FXML já estão prontos para uso.
5. Em produção, implemente cópia do arquivo enviado para uma pasta controlada (e não salvar caminhos absolutos apenas).
