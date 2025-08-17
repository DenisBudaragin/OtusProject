package main;
import asserts.CourseFinderAsserts;
import com.google.inject.Inject;
import helpers.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.junit.jupiter.api.Test;
import pages.CoursesPage;
import pages.MainPage;

import java.util.List;

import static helpers.Config.*;

public class CourseFinderTest extends BaseTest {
    @Test
    public void testCourseFinder() {
        // 1. Открыть страницу каталога курсов
        CoursesPage.openCoursesPage();

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
        CourseFinderAsserts courseFinderAsserts = new CourseFinderAsserts();
        courseFinderAsserts.assertCourseNameMatches(driver, COURSE_NAME);
    }
}

