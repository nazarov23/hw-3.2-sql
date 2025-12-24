package ru.netology.helpers;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHelper {
    private static Connection connection;
    private static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    private static void initConnection() {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://localhost:3306/app?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            connection = DriverManager.getConnection(url, "app", "pass");
        }
    }

    @SneakyThrows
    public static String getVerificationCodeForUser(String login) {
        initConnection();
        String sql = "SELECT code FROM auth_codes " +
                "WHERE user_id = (SELECT id FROM users WHERE login = ?) " +
                "ORDER BY created DESC LIMIT 1";
        return runner.query(connection, sql, new ScalarHandler<>(), login);
    }

    @SneakyThrows
    public static String getLastVerificationCode() {
        initConnection();
        String sql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        return runner.query(connection, sql, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void clearAuthCodes() {
        initConnection();
        runner.update(connection, "DELETE FROM auth_codes");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        initConnection();
        // Очищаем в порядке ОБРАТНОМ зависимостям (сначала дочерние таблицы)
        runner.update(connection, "DELETE FROM card_transactions"); // зависит от cards
        runner.update(connection, "DELETE FROM auth_codes");       // зависит от users
        runner.update(connection, "DELETE FROM cards");           // зависит от users
        runner.update(connection, "DELETE FROM users");           // основная таблица (очищаем последней)
        
    }

    @SneakyThrows
    public static void closeConnection() {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
