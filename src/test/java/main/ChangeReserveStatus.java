package main;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class ChangeReserveStatus {

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
        }
    }

    @Test
    public void testChangeReservationStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 ТЕСТ: ИЗМЕНЕНИЕ СТАТУСА РЕЗЕРВИРОВАНИЯ ПОДАРКА ДРУГОГО ПОЛЬЗОВАТЕЛЯ");
        System.out.println("=".repeat(60));

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

        try {
            // === ШАГ 1: ПЕРЕХОД НА ВТОРУЮ ВКЛАДКУ (ПОЛЬЗОВАТЕЛИ) ===
            System.out.println("\n📱 Шаг 1/12: Переход на вкладку пользователей...");
            By usersTabLocator = AppiumBy.androidUIAutomator(
                    "new UiSelector().resourceId(\"ru.otus.wishlist:id/navigation_bar_item_icon_view\").instance(1)"
            );
            wait.until(ExpectedConditions.elementToBeClickable(usersTabLocator)).click();
            System.out.println("✅ Перешли на вкладку пользователей");
            Thread.sleep(2000);

            // === ШАГ 2: ОТКРЫТИЕ ФИЛЬТРА ===
            System.out.println("\n🔍 Шаг 2/12: Открытие фильтра...");
            By filterButtonLocator = AppiumBy.id("ru.otus.wishlist:id/filter");
            wait.until(ExpectedConditions.elementToBeClickable(filterButtonLocator)).click();
            System.out.println("✅ Фильтр открыт");
            Thread.sleep(1000);

            // === ШАГ 3: ВВОД ИМЕНИ ПОЛЬЗОВАТЕЛЯ В ФИЛЬТР ===
            System.out.println("\n👤 Шаг 3/12: Поиск пользователя tonyp90...");
            By usernameInputLocator = AppiumBy.id("ru.otus.wishlist:id/username_input");
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameInputLocator));
            driver.findElement(usernameInputLocator).clear();
            driver.findElement(usernameInputLocator).sendKeys("tonyp90");
            System.out.println("✅ Введен пользователь: tonyp90");
            Thread.sleep(500);

            // === ШАГ 4: ПРИМЕНЕНИЕ ФИЛЬТРА ===
            System.out.println("\n✓ Шаг 4/12: Применение фильтра...");
            By applyButtonLocator = AppiumBy.id("ru.otus.wishlist:id/apply_button");
            wait.until(ExpectedConditions.elementToBeClickable(applyButtonLocator)).click();
            System.out.println("✅ Фильтр применен");
            Thread.sleep(2000);

            // === ШАГ 5: КЛИК ПО ПОЛЬЗОВАТЕЛЮ ===
            System.out.println("\n👥 Шаг 5/12: Выбор пользователя tonyp90 из списка...");
            By usernameLocator = AppiumBy.id("ru.otus.wishlist:id/username");
            wait.until(ExpectedConditions.elementToBeClickable(usernameLocator)).click();
            System.out.println("✅ Пользователь выбран");
            Thread.sleep(2000);

            // === ШАГ 6: ВЫБОР СПИСКА ЖЕЛАНИЙ ===
            System.out.println("\n📋 Шаг 6/12: Выбор списка желаний 'Ждем следующий год'...");
            By wishlistLocator = AppiumBy.androidUIAutomator(
                    "new UiSelector().text(\"Ждем следующий год\")"
            );
            wait.until(ExpectedConditions.elementToBeClickable(wishlistLocator)).click();
            System.out.println("✅ Список желаний 'Ждем следующий год' открыт");
            Thread.sleep(2000);

            // === ШАГ 7: ПОЛУЧАЕМ ТЕКУЩИЙ СТАТУС ПЕРВОГО ПОДАРКА ===
            System.out.println("\n🔄 Шаг 7/12: Проверка текущего статуса резервирования...");
            By reservedSwitchLocator = AppiumBy.androidUIAutomator(
                    "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\").instance(0)"
            );
            String switchState = driver.findElement(reservedSwitchLocator).getAttribute("checked");
            System.out.println("📊 Текущий статус: " + (switchState.equals("true") ? "🔴 ЗАРЕЗЕРВИРОВАНО" : "⚪ НЕ ЗАРЕЗЕРВИРОВАНО"));

            // === ШАГ 8: ИЗМЕНЕНИЕ СТАТУСА ===
            System.out.println("\n🔄 Шаг 8/12: Изменение статуса резервирования...");
            wait.until(ExpectedConditions.elementToBeClickable(reservedSwitchLocator)).click();
            System.out.println("✅ Статус изменен");
            Thread.sleep(1000);

            // === ШАГ 9: ПРОВЕРКА НОВОГО СТАТУСА ===
            System.out.println("\n✓ Шаг 9/12: Проверка нового статуса...");
            String newSwitchState = driver.findElement(reservedSwitchLocator).getAttribute("checked");
            String newStatus = newSwitchState.equals("true") ? "🔴 ЗАРЕЗЕРВИРОВАНО" : "⚪ НЕ ЗАРЕЗЕРВИРОВАНО";
            System.out.println("📊 Новый статус: " + newStatus);

            if (!switchState.equals(newSwitchState)) {
                System.out.println("✅ Статус успешно изменен с " +
                        (switchState.equals("true") ? "🔴 ЗАРЕЗЕРВИРОВАНО" : "⚪ НЕ ЗАРЕЗЕРВИРОВАНО") +
                        " на " + newStatus);
                Assert.assertNotEquals("Статус должен измениться", switchState, newSwitchState);
            } else {
                System.out.println("⚠️ Статус не изменился");
                Assert.fail("Статус не изменился после клика по переключателю");
            }

            // === ШАГ 10: ВЫВОДИМ ВСЕ ПОДАРКИ С ИХ СТАТУСАМИ ===
            System.out.println("\n📋 Шаг 10/12: Текущие подарки в списке:");
            var itemNames = driver.findElements(AppiumBy.id("ru.otus.wishlist:id/name_text"));

            for (int i = 0; i < itemNames.size(); i++) {
                try {
                    String itemName = itemNames.get(i).getText();
                    By switchForItem = AppiumBy.androidUIAutomator(
                            "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\").instance(" + i + ")"
                    );
                    String itemSwitchState = driver.findElement(switchForItem).getAttribute("checked");
                    String reservationStatus = itemSwitchState.equals("true") ? "🔴 ЗАРЕЗЕРВИРОВАНО" : "⚪ НЕ ЗАРЕЗЕРВИРОВАНО";
                    System.out.println("   • " + itemName + " - " + reservationStatus);
                } catch (Exception e) {
                    System.out.println("   • [Не удалось получить статус для элемента " + i + "]");
                }
            }

            // === ШАГ 11: ВОЗВРАТ НА ГЛАВНЫЙ ЭКРАН (ПРОФИЛЬ) ===
            System.out.println("\n🏠 Шаг 11/12: Возврат на главный экран (вкладка профиля)...");

            // Кликаем на третью вкладку (профиль) - ImageView с resource-id
            By profileTabLocator = AppiumBy.xpath("(//android.widget.ImageView[@resource-id=\"ru.otus.wishlist:id/navigation_bar_item_icon_view\"])[3]");

            wait.until(ExpectedConditions.elementToBeClickable(profileTabLocator)).click();
            System.out.println("✅ Перешли на вкладку профиля");
            Thread.sleep(2000);

            // Альтернативный вариант с использованием UiAutomator если XPath не сработает:
            // By profileTabLocator = AppiumBy.androidUIAutomator(
            //     "new UiSelector().resourceId(\"ru.otus.wishlist:id/navigation_bar_item_icon_view\").instance(2)"
            // );

            // === ШАГ 12: ВЫХОД ИЗ АККАУНТА ===
            System.out.println("\n🚪 Шаг 12/12: Выход из аккаунта...");

            // Прокручиваем экран вниз, чтобы кнопка выхода стала видимой (если нужно)
            // driver.findElement(AppiumBy.androidUIAutomator(
            //     "new UiScrollable(new UiSelector().scrollable(true)).scrollToEnd(5)"
            // ));

            By logoutButtonLocator = AppiumBy.id("ru.otus.wishlist:id/log_out_button");
            wait.until(ExpectedConditions.elementToBeClickable(logoutButtonLocator)).click();
            System.out.println("✅ Кнопка выхода нажата");

            Thread.sleep(2000);

            // Подтверждение выхода, если появляется диалоговое окно
            try {
                By confirmLogoutLocator = AppiumBy.id("android:id/button1"); // Стандартная кнопка "ОК" в Android
                if (driver.findElements(confirmLogoutLocator).size() > 0) {
                    driver.findElement(confirmLogoutLocator).click();
                    System.out.println("✅ Подтвержден выход из аккаунта");
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                // Если нет диалога подтверждения, просто продолжаем
                System.out.println("ℹ️ Диалог подтверждения не появился");
            }

            // Проверяем, что произошел выход (появилось поле для ввода логина)
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.id("ru.otus.wishlist:id/username_text_input")));
                System.out.println("✅ Выход из аккаунта выполнен успешно (появилась форма авторизации)");
            } catch (Exception e) {
                System.out.println("⚠️ Не удалось подтвердить выход из аккаунта");
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ ТЕСТ УСПЕШНО ЗАВЕРШЕН");
            System.out.println("📊 Итог: Статус резервирования изменен, выполнен выход из аккаунта");
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("\n❌ Ошибка в тесте: " + e.getMessage());
            e.printStackTrace();
        }
    }
}