package com.fiap.fintech.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DEFAULT_CONNECTION_STRING = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    public static Connection getConnection() throws SQLException {
        String connectionString = config("DB_URL", DEFAULT_CONNECTION_STRING);
        String user = requiredConfig("DB_USER");
        String password = requiredConfig("DB_PASSWORD");

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC Oracle nao encontrado no classpath do projeto.", e);
        }
        return DriverManager.getConnection(connectionString, user, password);
    }

    private static String config(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static String requiredConfig(String key) throws SQLException {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new SQLException("Configure a variavel de ambiente " + key + " para acessar o banco Oracle.");
        }
        return value;
    }
}
