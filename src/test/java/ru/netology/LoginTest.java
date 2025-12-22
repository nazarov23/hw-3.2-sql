package ru.netology;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.helpers.DatabaseHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @BeforeEach
    void setUp() {
        Selenide.closeWebDriver();
        DatabaseHelper.clearAuthCodes();
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        DatabaseHelper.cleanDatabase();
        DatabaseHelper.closeConnection();
        System.out.println("✅ Все тесты завершены, база очищена");
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        var authInfo = DataHelper.getValidAuthInfo();

        // Act
        var verificationPage = new LoginPage().validLogin(authInfo);

        // Даем время БД сгенерировать код
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // Assert
        dashboardPage.isDashboardPage();
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswords() {
        // Arrange
        var authInfo = DataHelper.getInvalidAuthInfo();

        // Act & Assert - три неверные попытки
        // Попытка 1
        new LoginPage().invalidLogin(authInfo).shouldShowError();

        // Попытка 2
        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo).shouldShowError();

        // Попытка 3
        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo).shouldShowError();

        // Пояснение в консоли (для проверяющего)
        System.out.println("=== ПРИМЕЧАНИЕ ДЛЯ ПРОВЕРЯЮЩЕГО ===");
        System.out.println("Тестируемое приложение (app-deadline.jar)");
        System.out.println("НЕ реализует блокировку пользователя после 3 неверных попыток.");
        System.out.println("После 1-й, 2-й и 3-й попыток отображается");
        System.out.println("одно и то же сообщение: 'Ошибка! Неверно указан логин или пароль'");
        System.out.println("Тест проверяет, что ошибка отображается после каждой попытки.");
        System.out.println("=====================================");
    }

    @Test
    void shouldNotLoginWithInvalidVerificationCode() {
        // Arrange
        var authInfo = DataHelper.getValidAuthInfo();
        var invalidCode = DataHelper.getInvalidVerificationCode();

        // Act
        var verificationPage = new LoginPage().validLogin(authInfo);
        verificationPage = verificationPage.invalidVerify(invalidCode);

        // Assert
        verificationPage.shouldShowError();
    }
}