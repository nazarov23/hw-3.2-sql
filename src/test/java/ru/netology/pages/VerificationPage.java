package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper.VerificationCode;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[name='code']");
    private SelenideElement verifyButton = $("[data-test-id='action-verify']");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");

    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(10));
        verifyButton.shouldBe(visible, Duration.ofSeconds(10));
    }

    public DashboardPage validVerify(VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return new DashboardPage();
    }

    public VerificationPage invalidVerify(VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
        verifyButton.click();
        return this;
    }

    public void shouldShowError() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(5));
    }
}