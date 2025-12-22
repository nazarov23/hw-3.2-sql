package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.VerificationCode;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[name='code']");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");
    private SelenideElement errorContent = $("[data-test-id='error-notification'] .notification__content");

    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(10));
        verifyButton.shouldBe(visible, Duration.ofSeconds(10));
    }

    // ОБЩИЙ МЕТОД для ввода кода (устраняет дублирование)
    private void enterVerificationCode(VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
    }

    public DashboardPage validVerify(VerificationCode verificationCode) {
        enterVerificationCode(verificationCode);
        return new DashboardPage();
    }

    public VerificationPage invalidVerify(VerificationCode verificationCode) {
        enterVerificationCode(verificationCode);
        return this;
    }

    // Проверка ошибки с КОНКРЕТНЫМ текстом
    public void shouldShowError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(5));
        // Проверяем точный текст ошибки
        errorContent.shouldHave(text("Ошибка! Неверно указан код! Попробуйте ещё раз."), Duration.ofSeconds(5));
    }
}