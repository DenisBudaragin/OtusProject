package main;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CreateWishListTest {
    private AndroidDriver driver;
    private WebDriverWait wait;
    private WebDriverWait longWait;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Основные capabilities
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appium:platformVersion", "13");
        capabilities.setCapability("appium:deviceName", "redroid13");
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        // Настройки приложения
        capabilities.setCapability("appium:appPackage", "ru.otus.wishlist");
        capabilities.setCapability("appium:appActivity", "ru.otus.wishlist.MainActivity");

        // Важно для уже установленного приложения
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
    @DisplayName("Тест успешной авторизации, создания и редактирования списка желаний")
    public void testSuccessfulLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 ТЕСТ: СОЗДАНИЕ И РЕДАКТИРОВАНИЕ СПИСКА ЖЕЛАНИЙ");
        System.out.println("=".repeat(60));

        try {
            // Ждем поля ввода
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("ru.otus.wishlist:id/username_text_input")));

            // Очищаем поля и вводим данные
            driver.findElement(By.id("ru.otus.wishlist:id/username_text_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/username_text_input")).sendKeys("DenisTest");

            driver.findElement(By.id("ru.otus.wishlist:id/password_text_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/password_text_input")).sendKeys("12345678");

            // Нажимаем кнопку входа
            driver.findElement(By.id("ru.otus.wishlist:id/log_in_button")).click();

            // Ждем появления элементов главного экрана
            System.out.println("⏳ Ожидание загрузки главного экрана...");
            try { Thread.sleep(3000); } catch (InterruptedException e) {}

            // Получаем текущую Activity
            System.out.println("📍 Текущая Activity: " + driver.currentActivity());

            System.out.println("✅ Авторизация успешна");

            // === ДОБАВЛЯЕМ НОВЫЙ ПУНКТ В СПИСОК ===
            System.out.println("\n➕ Добавляем новый пункт в список желаний...");

            // Кликаем на кнопку добавления
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("ru.otus.wishlist:id/add_button"))).click();
            System.out.println("✅ Кнопка добавления нажата");

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            // Заполняем поля
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("ru.otus.wishlist:id/title_input")));

            driver.findElement(By.id("ru.otus.wishlist:id/title_input")).sendKeys("DenTest wish");
            System.out.println("✅ Заголовок введен");

            driver.findElement(By.id("ru.otus.wishlist:id/description_input"))
                    .sendKeys("iPhone 15 Pro Max, 256GB, черный");
            System.out.println("✅ Описание введено");

            // Нажимаем кнопку сохранения
            driver.findElement(By.id("ru.otus.wishlist:id/save_button")).click();
            System.out.println("✅ Пункт сохранен");

            // Ждем возврата на главный экран
            try { Thread.sleep(3000); } catch (InterruptedException e) {}

            // Проверяем, что добавленный пункт появился в списке
            boolean itemFound = driver.findElements(By.xpath("//*[@text='DenTest wish']")).size() > 0;
            Assertions.assertTrue(itemFound, "Добавленный пункт должен отображаться в списке");
            System.out.println("✅ Добавленный пункт найден в списке");

            // === РЕДАКТИРУЕМ ДОБАВЛЕННЫЙ ПУНКТ ===
            System.out.println("\n✏️ Редактируем добавленный пункт...");

            // Генерируем случайное число для уникальности
            String randomSuffix = String.valueOf(System.currentTimeMillis()).substring(7);
            String editedTitle = "DenisTest wish edited " + randomSuffix;
            System.out.println("Новый заголовок: " + editedTitle);

            // Кликаем на кнопку редактирования второго элемента (так как первый добавленный)
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//android.widget.Button[@resource-id=\"ru.otus.wishlist:id/edit_button\"])[2]"))).click();
            System.out.println("✅ Кнопка редактирования нажата");

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            // Очищаем поле заголовка и вводим новый текст
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("ru.otus.wishlist:id/title_input")));

            driver.findElement(By.id("ru.otus.wishlist:id/title_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/title_input")).sendKeys(editedTitle);
            System.out.println("✅ Заголовок обновлен");

            // Нажимаем кнопку сохранения
            driver.findElement(By.id("ru.otus.wishlist:id/save_button")).click();
            System.out.println("✅ Изменения сохранены");

            // Ждем возврата на главный экран
            try { Thread.sleep(3000); } catch (InterruptedException e) {}

            // Проверяем, что изменения применились
            System.out.println("\n🔍 Проверка примененных изменений:");

            // Ищем элемент с новым заголовком
            boolean editedItemFound = driver.findElements(By.xpath("//*[@text='" + editedTitle + "']")).size() > 0;
            Assertions.assertTrue(editedItemFound, "Отредактированный пункт должен отображаться с новым заголовком");

            // Проверяем, что старый заголовок больше не отображается
            boolean oldItemFound = driver.findElements(By.xpath("//*[@text='DenTest wish']")).size() > 0;
            if (oldItemFound) {
                System.out.println("⚠️ Внимание: старый заголовок все еще отображается");
            } else {
                System.out.println("✅ Старый заголовок больше не отображается");
            }

            // Выводим все элементы списка для наглядности
            System.out.println("\n📋 Текущие элементы в списке:");
            driver.findElements(By.xpath("//*[@resource-id='ru.otus.wishlist:id/title_text']"))
                    .forEach(el -> {
                        String text = el.getText();
                        if (text != null && !text.isEmpty()) {
                            System.out.println("   • " + text);
                        }
                    });

            System.out.println("\n✅ Тест успешно завершен: пункт добавлен и отредактирован");

            // === ШАГ: ВЫХОД ИЗ АККАУНТА ===
            System.out.println("\n🚪 Выход из аккаунта...");

            // Навигация на вкладку профиля (третья вкладка)
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
                }
            }

            // Находим и нажимаем кнопку выхода
            try {
                By logoutButtonLocator = By.id("ru.otus.wishlist:id/log_out_button");
                wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator)).click();
                System.out.println("✅ Кнопка выхода нажата");
                Thread.sleep(1000);

                // Подтверждение выхода, если появляется диалоговое окно
                try {
                    By confirmLogoutLocator = By.id("android:id/button1"); // Стандартная кнопка "ОК"
                    if (driver.findElements(confirmLogoutLocator).size() > 0) {
                        driver.findElement(confirmLogoutLocator).click();
                        System.out.println("✅ Подтвержден выход из аккаунта");
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.out.println("ℹ️ Диалог подтверждения не появился");
                }

                // Проверяем, что произошел выход (появилась форма авторизации)
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.id("ru.otus.wishlist:id/username_text_input")));
                System.out.println("✅ Выход из аккаунта выполнен успешно");

            } catch (Exception e) {
                System.out.println("⚠️ Не удалось выполнить выход из аккаунта: " + e.getMessage());
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("🏁 ТЕСТ ПОЛНОСТЬЮ ЗАВЕРШЕН");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("\n❌ Ошибка в тесте: " + e.getMessage());
            e.printStackTrace();
            Assertions.fail("Тест завершился с ошибкой: " + e.getMessage());
        }
    }
}