package helpers;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Guice;
import java.util.List;

@Guice(modules = {WebDriverModule.class})
public class BaseTest {
    @Inject
    protected WebDriver driver;

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
}
