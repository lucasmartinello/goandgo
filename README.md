# goandgo

Projeto para cadastro de usuários e clientes.
Usando JDBC, Jersey, gradle, autenticação com JWT.
Criptografia de senha de usuário.

Favor criar um banco local no PostgreSQL chamado goandgo na porta 5432
Verificar configurações do banco nos arquivos:
- connection/DatabaseConnection.java (conexão com o banco)
- connection/FlywayConfig.java (criação das tabelas automaticamente)
