package main;
import helpers.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class CourseFinderTest extends WebDriverManager {

    private final String CATALOG_URL = "https://otus.ru/catalog/courses";
    private final String COURSE_NAME = "SRE практики и инструменты"; // Замените на имя курса для поиска
    private final String COURSE_XPATH = "//h6/div[contains(text(), 'SRE практики и инструменты')]"; // Замените на имя курса для поиска


    @Test
    public void testCourseFinder() {
        // 1. Открыть страницу каталога курсов
        driver.get(CATALOG_URL);

        // 2. Найти все элементы курсов
        List<WebElement> courseElements = driver.findElements(By.xpath(COURSE_XPATH));

        // 3. Используем Stream API для поиска курса по имени
        WebElement targetCourse = courseElements.stream()
                .filter(element -> {
                    String elementText = element.findElement(By.xpath(COURSE_XPATH)).getText();
                    return elementText.equalsIgnoreCase(COURSE_NAME);
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Курс с именем '" + COURSE_NAME + "' не найден"));

        // 4. Кликаем по найденному курсу
        targetCourse.click();

        // 5. Проверяем, что открыта страница верного курса
        String actualCourseName = driver.findElement(By.xpath("\n" +
                "//h1[text()='SRE практики и инструменты']")).getText();
        Assert.assertEquals(actualCourseName, COURSE_NAME,
                "Открыта страница неверного курса. Ожидалось: " + COURSE_NAME +
                        ", но получено: " + actualCourseName);
    }
}

