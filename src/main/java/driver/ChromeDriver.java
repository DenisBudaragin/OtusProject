package driver;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.concurrent.TimeUnit;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeDriver extends AbstractModule {
    Logger logger = LoggerFactory.getLogger(WebDriver.class);

    @Override
    protected void configure() {
        // Здесь можно забиндить другие зависимости
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        try {
            logger.info("Starting ChromeDriver setup...");
            WebDriverManager.chromedriver().setup();
            String driverPath = WebDriverManager.chromedriver().getDownloadedDriverPath();
            System.out.println("ChromeDriver path: " + driverPath);
            logger.info("ChromeDriver setup completed successfully");
        }
        catch (Exception e) {
            logger.error("ChromeDriver setup failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-infobars");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new org.openqa.selenium.chrome.ChromeDriver(options);
        driver.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }
}