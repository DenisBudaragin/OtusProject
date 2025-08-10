package helpers;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;

public class WebDriverModule extends AbstractModule{
    WebDriver driver = new ChromeDriver();
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        return driver;
    }
}
