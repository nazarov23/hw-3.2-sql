package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Selenide;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {
    private java.sql.Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        Selenide.closeWebDriver();

        // Подключаемся к БД для получения кода
        var url = "jdbc:mysql://localhost:3306/app?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        connection = DriverManager.getConnection(url, "app", "pass");

        // Очищаем старые коды верификации
        connection.createStatement().executeUpdate("DELETE FROM auth_codes WHERE created < NOW() - INTERVAL 1 HOUR;");
    }

    @Test
    void shouldLoginSuccessfully() throws SQLException {
        // 1. ОТКРЫВАЕМ СТРАНИЦУ
        open("http://localhost:9999");
        Selenide.sleep(1000);

        // 2. ВВОДИМ ЛОГИН И ПАРОЛЬ
        $("[name='login']").setValue("vasya");
        $("[name='password']").setValue("qwerty123");
        $("[data-test-id='action-login']").click();

        // 3. ЖДЕМ ПОЛЕ ДЛЯ КОДА
        $("[name='code']").shouldBe(Condition.visible, Duration.ofSeconds(5));
        System.out.println("✅ Первый этап пройден: появилось поле для кода");

        // 4. ПОЛУЧАЕМ КОД ИЗ БАЗЫ ДАННЫХ
        // Ждем немного, чтобы код успел сгенерироваться
        Selenide.sleep(2000);

        String codeSql = "SELECT code FROM auth_codes " +
                "WHERE user_id = (SELECT id FROM users WHERE login = 'vasya') " +
                "ORDER BY created DESC LIMIT 1";

        var result = connection.createStatement().executeQuery(codeSql);

        if (!result.next()) {
            // Если не нашли по конкретному пользователю, ищем любой код
            codeSql = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
            result = connection.createStatement().executeQuery(codeSql);

            if (!result.next()) {
                throw new RuntimeException("Код верификации не найден в БД!");
            }
        }

        String verificationCode = result.getString("code");
        System.out.println("✅ Получен код из БД: " + verificationCode);

        // 5. ВВОДИМ КОД ВЕРИФИКАЦИИ
        $("[name='code']").setValue(verificationCode);
        $("[data-test-id='action-verify']").click();

        // 6. ПРОВЕРЯЕМ, ЧТО ВОШЛИ В ЛИЧНЫЙ КАБИНЕТ
        // Ждем исчезновения поля для кода
        $("[name='code']").should(Condition.disappear, Duration.ofSeconds(5));

        // Простая проверка: ждем 3 секунды и проверяем, что поле кода исчезло
        Selenide.sleep(3000);

        // Делаем скриншот для проверки
        screenshot("personal_cabinet_result");
        System.out.println("✅ Тест пройден! Скриншот сохранен.");

        // Можно проверить URL или заголовок страницы
        System.out.println("Текущий URL: " + webdriver().driver().url());
        System.out.println("Заголовок страницы: '" + title() + "'");
    }
}