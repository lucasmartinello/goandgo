package org.goandgo.connection;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/goandgo";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";

    private static DatabaseConnection instance;
    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver"); // Carrega o driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado!", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}