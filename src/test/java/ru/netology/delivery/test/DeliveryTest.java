package ru.netology.delivery.test;

import io.qameta.allure.selenide.AllureSelenide;
import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.logevents.SelenideLogger;
import org.openqa.selenium.Keys;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.delivery.data.DataGenerator.*;

class DeliveryTest {
    private final UserInfo validUser = generateValidUser();

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendFormValid() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDate(8)));

        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(9));
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Необходимо подтверждение"));

        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(exactText("Успешно! Встреча успешно запланирована на " + generateDate(9)));
    }

    @Test
    void shouldSendFormWithWrongSurname() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue("Ivanov Иван");
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithWrongCity() {
        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='city'] .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithWrongDate() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys("18.02.2021");
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithEmptyDate() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub")
                .shouldHave(exactText("Неверно введена дата"))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithEmptyName() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithEmptyPhone() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue("");
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"))
                .shouldBe(visible);
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(generateDate(8));
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $(byText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"))
                .shouldBe(visible);
    }
}
