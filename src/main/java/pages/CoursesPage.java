package pages;

import helpers.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import static configa.Config.OTUS_COURSES_PAGE;

public class CoursesPage extends BaseTest {
    public static void open() {
        driver.get(OTUS_COURSES_PAGE);
    }

    public static void findAndClickCourseByName(String courseName, String courseXpath) {
        // Ожидание загрузки страницы
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });

        List<WebElement> courseElements = driver.findElements(By.xpath(courseXpath));

        WebElement targetCourse = courseElements.stream()
                .filter(element -> {
                    String elementText = element.getText();
                    return elementText.equalsIgnoreCase(courseName);
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("Курс с именем '" + courseName + "' не найден"));

        targetCourse.click();
    }
}
