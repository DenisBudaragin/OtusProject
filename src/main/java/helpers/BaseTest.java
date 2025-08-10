package helpers;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Guice;

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
}
