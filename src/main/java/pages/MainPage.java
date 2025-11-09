package pages;

import helpers.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import static configs.Config.OTUS_MAIN_PAGE;

public class MainPage extends BaseTest {
    public static void open() {
        driver.get(OTUS_MAIN_PAGE);
    }

    public static void clickOnCategory(String categoryName) {
        // Ожидание загрузки страницы
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> {
            return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        });
        WebElement categoryLink = findElement(
                By.xpath(String.format("//*[text()='%s']", categoryName))
        );
        categoryLink.click();
    }

    public static String clickRandomCourseInCategory(String categoryXpath) {
        List<WebElement> courseElements = driver.findElements(By.xpath(categoryXpath));
        assertFalse(courseElements.isEmpty(), "В категории нет доступных курсов");
        Random random = new Random();
        int randomIndex = random.nextInt(courseElements.size());
        WebElement randomElement = courseElements.get(randomIndex);
        String expectedUrl = getElementUrl(randomElement);
        randomElement.click();
        return expectedUrl;
    }

    public static String getElementUrl(WebElement element) {
        return element.getAttribute("href");
    }

    public static String getCurrentPageUrl() {
        return driver.getCurrentUrl();
    }
}
