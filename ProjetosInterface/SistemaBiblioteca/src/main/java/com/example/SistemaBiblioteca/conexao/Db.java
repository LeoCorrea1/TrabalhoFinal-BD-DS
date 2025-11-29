package com.example.SistemaBiblioteca.conexao;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class Db {
    private static final String URL = "jdbc:mysql://localhost:3306/BibliotecaDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "leo231105";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
