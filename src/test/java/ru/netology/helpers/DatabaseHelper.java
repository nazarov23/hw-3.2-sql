package ru.netology.helpers;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseHelper {
    private static Connection connection;

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
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("code") : null;
        }
    }

    @SneakyThrows
    public static String getLastVerificationCode() {
        initConnection();
        String sql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getString("code") : null;
        }
    }

    @SneakyThrows
    public static void clearAuthCodes() {
        initConnection();
        connection.createStatement().executeUpdate("DELETE FROM auth_codes");
    }

    @SneakyThrows
    public static void closeConnection() {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}