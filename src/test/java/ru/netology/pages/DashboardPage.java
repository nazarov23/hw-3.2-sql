package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");

    public DashboardPage() {
        heading.shouldBe(visible, Duration.ofSeconds(10));
    }

    public void isDashboardPage() {
        heading.shouldBe(visible);
    }
}