import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1920x1080";
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitFormWithValidData() {
        // 1. Генерируем дату: текущая дата + 3 дня
        String planningDate = LocalDate.now()
                .plusDays(4)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // 2. Заполняем поля
        $("[data-test-id='city'] input").setValue("Казань");

        // ВАЖНО: поле даты содержит значение по умолчанию,
        // поэтому сначала очищаем его комбинацией Shift+Home и Backspace,
        // и только потом вводим сгенерированную строку
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);

        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79000000000");

        // 3. Ставим галочку согласия
        $("[data-test-id='agreement']").click();

        // 4. Нажимаем "Забронировать" — ищем кнопку по тексту
        $$("button").findBy(text("Забронировать")).click();

        // 5. Проверяем всплывающее окно
        $("[data-test-id='notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + planningDate));
    }
}