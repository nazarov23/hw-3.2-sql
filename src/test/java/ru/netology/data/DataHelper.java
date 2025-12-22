package ru.netology.data;

import lombok.Value;
import ru.netology.helpers.DatabaseHelper;

public class DataHelper {
    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    // Тестовые данные
    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo("vasya", "wrongpassword");
    }

    // Получение кода из БД через DatabaseHelper
    public static VerificationCode getVerificationCode() {
        String code = DatabaseHelper.getVerificationCodeForUser("vasya");
        if (code == null) {
            // Если не нашли для vasya, ищем любой код
            code = DatabaseHelper.getLastVerificationCode();
        }
        if (code == null) {
            throw new RuntimeException("Код верификации не найден в БД!");
        }
        return new VerificationCode(code);
    }

    public static VerificationCode getInvalidVerificationCode() {
        return new VerificationCode("000000");
    }
}