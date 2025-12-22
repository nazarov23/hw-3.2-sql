package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id='dashboard']");

    public DashboardPage() {
        // При создании страницы ждем появления заголовка
        heading.shouldBe(visible, Duration.ofSeconds(10));
    }

    // Проверка, что мы действительно на странице "Личный кабинет"
    public void isDashboardPage() {
        // 1. Проверяем видимость элемента
        heading.shouldBe(visible);
        // 2. Проверяем точный текст "Личный кабинет"
        heading.shouldHave(text("Личный кабинет"), Duration.ofSeconds(5));
    }
}