package com.fiap.fintech.factory;

import com.fiap.fintech.config.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DEFAULT_CONNECTION_STRING = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    public static Connection getConnection() throws SQLException {
        String connectionString = Environment.get("DB_URL", DEFAULT_CONNECTION_STRING);
        String user;
        String password;

        try {
            user = Environment.require("DB_USER");
            password = Environment.require("DB_PASSWORD");
        } catch (IllegalStateException exception) {
            throw new SQLException(exception.getMessage(), exception);
        }

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC Oracle nao encontrado no classpath do projeto.", e);
        }
        return DriverManager.getConnection(connectionString, user, password);
    }
}
