package main;

import io.appium.java_client.android.AndroidDriver; // ИЗМЕНЕНО: конкретный тип
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AndroidLoginTest {
    private AndroidDriver driver; // ИЗМЕНЕНО: с AppiumDriver на AndroidDriver
    private WebDriverWait wait;
    private WebDriverWait longWait;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Основные capabilities
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:platformVersion", "13");
        capabilities.setCapability("appium:deviceName", "redroid13_x86_64");
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        // Настройки приложения
        capabilities.setCapability("appium:appPackage", "ru.otus.wishlist");
        capabilities.setCapability("appium:appActivity", "ru.otus.wishlist.MainActivity");
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

        // Подключаемся к Appium серверу
        driver = new AndroidDriver(new URL("http://109.73.196.103:4723"), capabilities);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        longWait = new WebDriverWait(driver, Duration.ofSeconds(60));

        System.out.println("✅ Сессия создана!");
        System.out.println("Session ID: " + driver.getSessionId());
        System.out.println("⏱️ Таймауты: основное ожидание 30с, длинное 60с");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Сессия закрыта");
        }
    }

    @Test
    @DisplayName("Тест успешной авторизации")
    public void testSuccessfulLogin() {
        // Ждем поля ввода
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("ru.otus.wishlist:id/username_text_input")));

        // Очищаем поля и вводим данные
        driver.findElement(By.id("ru.otus.wishlist:id/username_text_input")).clear();
        driver.findElement(By.id("ru.otus.wishlist:id/username_text_input")).sendKeys("to");

        driver.findElement(By.id("ru.otus.wishlist:id/password_text_input")).clear();
        driver.findElement(By.id("ru.otus.wishlist:id/password_text_input")).sendKeys("12345678");

        // Нажимаем кнопку входа
        driver.findElement(By.id("ru.otus.wishlist:id/log_in_button")).click();

        // Ждем появления элементов главного экрана и выводим их
        System.out.println("⏳ Ожидание загрузки главного экрана...");

        // Даем время на загрузку
        try { Thread.sleep(3000); } catch (InterruptedException e) {}

        // Получаем текущую Activity
        System.out.println("📍 Текущая Activity: " + driver.currentActivity());

        // Выводим все видимые элементы с resource-id
        System.out.println("\n🔍 Элементы на главном экране:");
        driver.findElements(By.xpath("//*[@resource-id]"))
                .stream()
                .filter(el -> el.getAttribute("resource-id") != null &&
                        !el.getAttribute("resource-id").isEmpty())
                .limit(30)
                .forEach(el -> {
                    String rid = el.getAttribute("resource-id");
                    String text = el.getText();
                    String className = el.getAttribute("class");

                    System.out.println("   📌 ID: " + rid);
                    if (text != null && !text.isEmpty()) {
                        System.out.println("      Текст: " + text);
                    }
                    if (className != null) {
                        System.out.println("      Класс: " + className);
                    }
                });

        // Выводим все текстовые элементы
        System.out.println("\n📝 Текстовые элементы:");
        driver.findElements(By.xpath("//*[@text!='']"))
                .stream()
                .limit(20)
                .forEach(el -> {
                    String text = el.getText();
                    String rid = el.getAttribute("resource-id");
                    System.out.println("   Текст: \"" + text + "\" (ID: " + rid + ")");
                });

        // Проверяем, что мы действительно перешли на другой экран
        boolean isMainScreen = !driver.currentActivity().contains("MainActivity");
        Assertions.assertTrue(isMainScreen,
                "Должен быть переход на главный экран, текущая Activity: " + driver.currentActivity());

        System.out.println("✅ Тест успешной авторизации пройден");
    }

//    @Test
//    @DisplayName("Тест авторизации с пустыми полями")
//    public void testLoginWithEmptyFields() {
//        // Ждем поля ввода
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.id("ru.otus.wishlist:id/username_text_input")));
//
//        // Оставляем поля пустыми и нажимаем кнопку
//        driver.findElement(By.id("ru.otus.wishlist:id/username_text_input")).clear();
//        driver.findElement(By.id("ru.otus.wishlist:id/password_text_input")).clear();
//        driver.findElement(By.id("ru.otus.wishlist:id/log_in_button")).click();
//
//        // Ждем сообщение об ошибке (замените ID на актуальный)
//        wait.until(ExpectedConditions.presenceOfElementLocated(
//                By.id("\t\n" +
//                        "ru.otus.wishlist:id/alertTitle")));
//
//        System.out.println("✅ Тест пройден: сообщение об ошибке появилось");
//    }
//
//    @Test
//    @DisplayName("Диагностика приложения")
//    public void diagnosticTest() {
//        try {
//            Thread.sleep(5000);
//
//            // ТЕПЕРЬ ЭТИ МЕТОДЫ РАБОТАЮТ
//            System.out.println("📍 Текущая Activity: " + driver.currentActivity());
//            System.out.println("📦 Текущий Package: " + driver.getCurrentPackage());
//
//            longWait.until(ExpectedConditions.presenceOfElementLocated(
//                    By.xpath("//*[@resource-id]")));
//
//            System.out.println("🔍 Найденные resource-id на экране:");
//            driver.findElements(By.xpath("//*[@resource-id]"))
//                    .stream()
//                    .limit(20)
//                    .forEach(el -> {
//                        String rid = el.getAttribute("resource-id");
//                        if (rid != null && !rid.isEmpty()) {
//                            System.out.println("   - " + rid);
//                        }
//                    });
//
//            // ДОБАВЛЕНО: Поиск текстовых полей
//            System.out.println("\n🔍 Поиск текстовых полей:");
//            driver.findElements(By.xpath("//*[@class='android.widget.EditText']"))
//                    .forEach(el -> {
//                        System.out.println("   Поле ввода: " + el.getAttribute("resource-id"));
//                    });
//
//            // ДОБАВЛЕНО: Поиск кнопок
//            System.out.println("\n🔍 Поиск кнопок:");
//            driver.findElements(By.xpath("//*[@class='android.widget.Button']"))
//                    .forEach(el -> {
//                        System.out.println("   Кнопка: " + el.getAttribute("resource-id") +
//                                ", текст: " + el.getText());
//                    });
//
//            System.out.println("✅ Диагностика завершена");
//
//        } catch (Exception e) {
//            System.err.println("❌ Ошибка диагностики: " + e.getMessage());
//        }
//    }
}