package driver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import configa.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverModule extends AbstractModule{
    private static final Logger log = LoggerFactory.getLogger(WebDriverModule.class);

    @Provides
    @Singleton
    public WebDriver provideWebDriver() {
        return ConfigReader.isRemoteMode()
                ? createRemoteDriver()
                : createLocalDriver();
    }

    private WebDriver createLocalDriver() {
        log.info("Инициализация локального ChromeDriver");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = getDefaultOptions();
        WebDriver driver = new org.openqa.selenium.chrome.ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }

    private WebDriver createRemoteDriver() {
        String remoteUrl = ConfigReader.getProperty("remote.url");
        if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
            throw new RuntimeException("remote.url не указан для удалённого режима!");
        }

        // Получаем версию браузера из конфигурации
        String browserVersion = ConfigReader.getProperty("browser.version");
        if (browserVersion == null || browserVersion.trim().isEmpty()) {
            throw new RuntimeException("browser.version не указан для удалённого режима!");
        }

        // Получаем параметры Selenoid из конфигурации
        boolean enableVNC = Boolean.parseBoolean(ConfigReader.getProperty("selenoid.enableVNC", "false"));
        boolean enableVideo = Boolean.parseBoolean(ConfigReader.getProperty("selenoid.enableVideo", "false"));
        String sessionTimeout = ConfigReader.getProperty("selenoid.sessionTimeout", "15m");

        log.info("Подключение к Selenoid: {}, версия браузера: {}", remoteUrl, browserVersion);
        ChromeOptions options = getDefaultOptions();

        // Используем версию из конфигурации
        options.setCapability("browserVersion", browserVersion);
        log.info("Запрашиваемая версия браузера: {}", browserVersion);

        // Selenoid-specific capabilities из конфигурации
        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVNC", enableVNC);
        selenoidOptions.put("enableVideo", enableVideo);
        selenoidOptions.put("sessionTimeout", sessionTimeout);
        options.setCapability("selenoid:options", selenoidOptions);

        try {
            URL url = new URL(remoteUrl);
            WebDriver driver = new RemoteWebDriver(url, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            log.info("Сессия в Selenoid успешно создана. Session ID: {}", ((RemoteWebDriver) driver).getSessionId());
            return driver;
        } catch (Exception e) {
            log.error("Ошибка подключения к Selenoid (версия {}):", browserVersion, e);
            throw new RuntimeException(
                    "Ошибка создания сессии Chrome " + browserVersion + " в Selenoid.\n" +
                            "Возможные причины:\n" +
                            "  1. Образ selenoid/chrome:" + browserVersion + " отсутствует на сервере\n" +
                            "  2. Проверьте: docker images | grep chrome\n" +
                            "  3. Проверьте доступность указанной версии в конфигурации",
                    e
            );
        }
    }

    private ChromeOptions getDefaultOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.setAcceptInsecureCerts(true);
        return options;
    }
}
