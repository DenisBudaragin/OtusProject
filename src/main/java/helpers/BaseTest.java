package helpers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import driver.WebDriverModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class BaseTest {
    protected WebDriver driver; // убрали static
    private Injector injector; // убрали static
    private static BaseTest instance;

    private BaseTest() {}

    public static BaseTest getInstance() {
        if (instance == null) {
            instance = new BaseTest();
        }
        return instance;
    }

    public void setup() { // убрали static
        if (driver == null) {
            injector = Guice.createInjector(new WebDriverModule()); // исправил на модуль
            driver = injector.getInstance(WebDriver.class);
        }
    }

    public void teardown() { // убрали static
        if (driver != null) {
            driver.quit();
            driver = null;
            injector = null;
        }
    }

    public WebDriver getDriver() { // убрали static
        return driver;
    }

    public Injector getInjector() { // убрали static
        return injector;
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public WebElement findElement(By locator) { // убрали static
        return driver.findElement(locator);
    }
}
