package driver;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WebDriverModule extends AbstractModule {
    Logger logger = LoggerFactory.getLogger(WebDriver.class);

    @Override
    protected void configure() {
        // Здесь можно забиндить другие зависимости
    }

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        WebDriver driver = null;

        try {
            logger.info("Starting WebDriver setup...");

            // Автоматическое определение и настройка драйвера для текущей ОС
            WebDriverManager manager = WebDriverManager.chromedriver();

            // Кросс-платформенные настройки
            manager.setup();

            String driverPath = manager.getDownloadedDriverPath();
            String driverVersion = manager.getDownloadedDriverVersion();

            logger.info("WebDriver path: {}", driverPath);
            logger.info("WebDriver version: {}", driverVersion);
            logger.info("WebDriver setup completed successfully");

            // Кросс-платформенные опции Chrome
            ChromeOptions options = createChromeOptions();

            // Создание драйвера
            driver = new ChromeDriver(options);

            // Кросс-платформенные таймауты
            setupDriverTimeouts(driver);

            logger.info("WebDriver initialized successfully for OS: {}",
                    System.getProperty("os.name"));

        } catch (Exception e) {
            logger.error("WebDriver setup failed: {}", e.getMessage(), e);
            if (driver != null) {
                driver.quit();
            }
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }

        return driver;
    }

    private ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Базовые аргументы, совместимые со всеми ОС
        List<String> arguments = new ArrayList<>();

        // Универсальные аргументы
        arguments.add("--start-maximized");
        arguments.add("--disable-infobars");
        arguments.add("--disable-extensions");
        arguments.add("--disable-notifications");
        arguments.add("--disable-gpu");
        arguments.add("--no-default-browser-check");
        arguments.add("--no-first-run");
        arguments.add("--disable-background-timer-throttling");
        arguments.add("--disable-popup-blocking");
        arguments.add("--disable-translate");

        // Аргументы для стабильности на всех ОС
        arguments.add("--no-sandbox");
        arguments.add("--disable-dev-shm-usage");
        arguments.add("--remote-allow-origins=*");

        // Определяем ОС и добавляем специфичные настройки
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            // Windows-specific options
            arguments.add("--disable-features=RendererCodeIntegrity");
        } else if (osName.contains("linux")) {
            // Linux-specific options
            arguments.add("--disable-setuid-sandbox");
            arguments.add("--disable-web-security");
            arguments.add("--allow-running-insecure-content");
            // Для headless режима на Linux (если понадобится)
            // arguments.add("--headless");
        } else if (osName.contains("mac")) {
            // macOS-specific options
            arguments.add("--disable-blink-features=AutomationControlled");
        }

        options.addArguments(arguments);

        // Дополнительные кросс-платформенные настройки
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        // Для лучшей совместимости с разными версиями Chrome
        options.setExperimentalOption("excludeSwitches",
                new String[]{"enable-automation", "load-extension"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Установка пользовательского агента для лучшей кросс-платформенной совместимости
        options.addArguments("--user-agent=Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        return options;
    }

    private void setupDriverTimeouts(WebDriver driver) {
        // Универсальные таймауты для всех ОС
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10))
                .pageLoadTimeout(Duration.ofSeconds(30))
                .scriptTimeout(Duration.ofSeconds(30));
    }
    // Дополнительный метод для headless режима (опционально)
    private ChromeOptions createHeadlessChromeOptions() {
        ChromeOptions options = createChromeOptions();

        // Headless аргументы, работающие на всех ОС
        options.addArguments("--headless=new"); // Новый headless режим
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-software-rasterizer");

        return options;
    }
}