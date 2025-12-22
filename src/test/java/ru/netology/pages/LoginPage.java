package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.AuthInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[name='login']");
    private SelenideElement passwordField = $("[name='password']");
    private SelenideElement loginButton = $("[data-test-id='action-login']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");
    private SelenideElement errorContent = $("[data-test-id='error-notification'] .notification__content");

    public LoginPage() {
        loginField.shouldBe(visible, Duration.ofSeconds(10));
        passwordField.shouldBe(visible, Duration.ofSeconds(10));
        loginButton.shouldBe(visible, Duration.ofSeconds(10));
    }

    // ОБЩИЙ МЕТОД для ввода логина/пароля (принцип DRY)
    private void enterCredentials(AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
    }

    public VerificationPage validLogin(AuthInfo authInfo) {
        enterCredentials(authInfo);
        return new VerificationPage();
    }

    public LoginPage invalidLogin(AuthInfo authInfo) {
        enterCredentials(authInfo);
        return this;
    }

    // Проверка обычной ошибки (неверный логин/пароль) с КОНКРЕТНЫМ текстом
    public void shouldShowError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(5));
        // Проверяем ТОЧНЫЙ текст ошибки из вашего приложения
        errorContent.shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(5));
    }

    // Проверка сообщения о БЛОКИРОВКЕ системы
    public void shouldShowBlockedMessage() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(5));
        errorContent.shouldHave(text("система заблокирована"), Duration.ofSeconds(5));
    }
}