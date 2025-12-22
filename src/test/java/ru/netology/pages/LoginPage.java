package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.AuthInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[name='login']");
    private SelenideElement passwordField = $("[name='password']");
    private SelenideElement loginButton = $("[data-test-id='action-login']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public LoginPage() {
        loginField.shouldBe(visible, Duration.ofSeconds(10));
        passwordField.shouldBe(visible, Duration.ofSeconds(10));
        loginButton.shouldBe(visible, Duration.ofSeconds(10));
    }

    public VerificationPage validLogin(AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public LoginPage invalidLogin(AuthInfo authInfo) {
        loginField.setValue(authInfo.getLogin());
        passwordField.setValue(authInfo.getPassword());
        loginButton.click();
        return this;
    }

    public void shouldShowError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(5));
    }
}