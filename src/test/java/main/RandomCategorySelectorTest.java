package main;
import helpers.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.util.Random;
import java.util.List;
import static asserts.Assertions.assertUrlsAfterDelimitersEqual;
import static helpers.Config.*;
import static org.junit.jupiter.api.Assertions.*;

public class RandomCategorySelectorTest extends WebDriverManager {
    @Test
    public void shouldReturnCourseWithValidCategory() {
        driver.get(OTUS_MAIN_PAGE);
        new WebDriverWait(driver, 10)
                .until(d -> ((JavascriptExecutor)d)
                        .executeScript("return document.readyState").equals("complete"));

        // 2. Находим и кликаем по разделу "Обучение"
        WebElement categoryLink = driver.findElement(
                By.xpath(String.format("//*[text()='Обучение']", CATEGORY_NAME))
        );
        categoryLink.click();

        // 3. Собираем все курсы в категории
        List<WebElement> courseElements = driver.findElements(By.xpath("\n" +
                "//*[@id=\"__next\"]/div[1]/div[1]/div/nav/div[3]/div/div/div[1]/div/div/a"));
        // Проверяем что курсы есть
        assertFalse(courseElements.isEmpty(), "В категории нет доступных курсов");

        // 3. Генерируем случайный индекс
        Random random = new Random();
        int randomIndex = random.nextInt(courseElements.size());

        // 4. Получаем случайный элемент
        WebElement randomElement = courseElements.get(randomIndex);
        String expectedUrl = randomElement.getAttribute("href");

        // 5. Кликаем на элемент
        randomElement.click();
        String actualUrl = driver.getCurrentUrl();

        // 7. Проверяем что мы на правильной странице
        assertUrlsAfterDelimitersEqual(actualUrl, expectedUrl);
    }
}
