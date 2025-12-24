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
        DatabaseHelper.clearAuthCodes();
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        DatabaseHelper.cleanDatabase();
        DatabaseHelper.closeConnection();
    }

    @Test
    void shouldLoginSuccessfully() {
        var authInfo = DataHelper.getValidAuthInfo();
        var verificationPage = new LoginPage().validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.isDashboardPage();
    }

    @Test
    void shouldShowErrorAfterEachOfThreeInvalidPasswordAttempts() {
        var authInfo = DataHelper.getInvalidAuthInfo();

        new LoginPage().invalidLogin(authInfo)
                .shouldShowErrorMessage("Ошибка! Неверно указан логин или пароль");

        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo)
                .shouldShowErrorMessage("Ошибка! Неверно указан логин или пароль");

        open("http://localhost:9999");
        new LoginPage().invalidLogin(authInfo)
                .shouldShowErrorMessage("Ошибка! Неверно указан логин или пароль");
    }

    @Test
    void shouldNotLoginWithInvalidVerificationCode() {
        var authInfo = DataHelper.getValidAuthInfo();
        var invalidCode = DataHelper.getInvalidVerificationCode();

        new LoginPage()
                .validLogin(authInfo)
                .invalidVerify(invalidCode)
                .shouldShowErrorMessage("Ошибка! Неверно указан код! Попробуйте ещё раз.");
    }
}