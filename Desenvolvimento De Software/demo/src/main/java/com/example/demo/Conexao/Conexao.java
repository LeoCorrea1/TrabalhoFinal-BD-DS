package com.example.demo.Conexao;


import java.sql.Connection;
import java.sql.DriverManager;


public class Conexao {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BibliotecaDB;encrypt=false;";
    private static final String USER = "sa";        // seu usuário do SQL Server
    private static final String PASSWORD = "laboratorio"; // sua senha

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
