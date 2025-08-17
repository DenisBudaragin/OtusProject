package driver;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.concurrent.TimeUnit;

public class WebDriverModule extends AbstractModule {

    @Override
    protected void configure() {
        // Здесь можно забиндить другие зависимости
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        // Настройка пути к драйверу (лучше вынести в конфиг)
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        // Настройка опций браузера
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }
}