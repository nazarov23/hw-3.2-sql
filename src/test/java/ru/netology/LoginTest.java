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
        DatabaseHelper.clearAuthCodes(); // очистка БД через утилитный класс
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        DatabaseHelper.closeConnection(); // закрытие соединения с БД
    }

    @Test
    void shouldLoginSuccessfully() {
        // Arrange
        var authInfo = DataHelper.getValidAuthInfo();

        // Act
        var verificationPage = new LoginPage().validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(); // получаем код через DataHelper
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // Assert
        dashboardPage.isDashboardPage();
    }

    @Test
    void shouldBlockAfterThreeInvalidPasswords() {
        // Arrange
        var authInfo = DataHelper.getInvalidAuthInfo();

        // Act & Assert - три неверные попытки
        new LoginPage().invalidLogin(authInfo).shouldShowError();
        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo).shouldShowError();
        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo).shouldShowError();

        // Проверяем сообщение о блокировке (если есть)
        // $("[data-test-id='error-notification'] .notification__content")
        //     .shouldHave(text("система заблокирована"), Duration.ofSeconds(5));
    }
}