package helpers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import driver.ChromeDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BaseTest {
    protected static WebDriver driver;
    private static Injector injector;

    @BeforeAll
    public static void setup() {
        injector = Guice.createInjector(new ChromeDriver());
        driver = injector.getInstance(WebDriver.class);
    }

    @AfterAll
    public static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static Injector getInjector() {
        return injector;
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public static WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
}
