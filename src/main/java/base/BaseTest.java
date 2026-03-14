package base;

import helpers.RandomDataGenerator;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.ProfilePage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait longWait;
    protected LoginPage loginPage;
    protected ProfilePage profilePage;
    protected RandomDataGenerator randomDataGenerator;

    protected static final String TEST_USERNAME = "DenisTest";
    protected static final String TEST_PASSWORD = "12345678";
    protected static final String APP_PACKAGE = "ru.otus.wishlist";
    protected static final String APP_ACTIVITY = "ru.otus.wishlist.MainActivity";
    protected static final String APPIUM_SERVER_URL = "http://109.73.196.103:4723";

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Основные capabilities
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:platformVersion", "13");
        capabilities.setCapability("appium:deviceName", "redroid13");
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        // Настройки приложения
        capabilities.setCapability("appium:appPackage", APP_PACKAGE);
        capabilities.setCapability("appium:appActivity", APP_ACTIVITY);
        capabilities.setCapability("appium:noReset", true);
        capabilities.setCapability("appium:fullReset", false);
        capabilities.setCapability("appium:appWaitForLaunch", true);
        capabilities.setCapability("appium:appWaitDuration", 60000);

        // Настройки разрешений
        capabilities.setCapability("appium:autoGrantPermissions", true);
        capabilities.setCapability("appium:autoAcceptAlerts", true);

        // Таймауты
        capabilities.setCapability("appium:newCommandTimeout", 600);
        capabilities.setCapability("appium:adbExecTimeout", 60000);
        capabilities.setCapability("appium:uiautomator2ServerInstallTimeout", 120000);
        capabilities.setCapability("appium:androidInstallTimeout", 120000);

        driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
        loginPage = new LoginPage(driver);
        profilePage = new ProfilePage(driver);
        randomDataGenerator = new RandomDataGenerator();

        System.out.println("✅ Сессия создана!");
        System.out.println("Session ID: " + driver.getSessionId());

        try {
            // Если мы уже на главном экране (не на логине), выполняем выход
            if (!loginPage.isLoginScreenDisplayed()) {
                System.out.println("⚠️ Обнаружена активная сессия, выполняем выход перед тестом...");
                performLogout();
            }
        } catch (Exception e) {
            System.out.println("ℹ️ Проверка состояния перед тестом: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        try {
            if (driver != null) {
                System.out.println("\n🚪 Выход из аккаунта после теста...");
                performLogout();
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка в процессе выхода из аккаунта: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
                System.out.println("✅ Сессия закрыта");
                System.out.println("\n" + "=".repeat(60));
                System.out.println("🏁 ТЕСТ ПОЛНОСТЬЮ ЗАВЕРШЕН");
                System.out.println("=".repeat(60));
            }
        }
    }

    protected void performLogout() {
        try {
            try {
                By profileTabLocator = By.xpath("(//android.widget.ImageView[@resource-id=\"ru.otus.wishlist:id/navigation_bar_item_icon_view\"])[3]");
                wait.until(ExpectedConditions.elementToBeClickable(profileTabLocator)).click();
                System.out.println("✅ Перешли на вкладку профиля");
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("⚠️ Не удалось перейти на вкладку профиля через XPath, пробуем альтернативный способ");
                try {
                    By profileTabLocatorAlt = By.xpath("//android.widget.FrameLayout[@content-desc=\"Profile\"]");
                    wait.until(ExpectedConditions.elementToBeClickable(profileTabLocatorAlt)).click();
                    System.out.println("✅ Перешли на вкладку профиля (альтернативный способ)");
                    Thread.sleep(1000);
                } catch (Exception e2) {
                    System.out.println("⚠️ Не удалось найти вкладку профиля");
                    return; // Выходим из метода, если не можем найти профиль
                }
            }
            try {
                By logoutButtonLocator = By.id("ru.otus.wishlist:id/log_out_button");
                wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator)).click();
                System.out.println("✅ Кнопка выхода нажата");
                Thread.sleep(1000);
                try {
                    By confirmLogoutLocator = By.id("android:id/button1");
                    if (driver.findElements(confirmLogoutLocator).size() > 0) {
                        driver.findElement(confirmLogoutLocator).click();
                        System.out.println("✅ Подтвержден выход из аккаунта");
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.out.println("ℹ️ Диалог подтверждения не появился");
                }
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("ru.otus.wishlist:id/username_text_input")));
                System.out.println("✅ Выход из аккаунта выполнен успешно");

            } catch (Exception e) {
                System.out.println("⚠️ Не удалось выполнить выход из аккаунта: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Ошибка при выходе из аккаунта: " + e.getMessage());
        }
    }
}