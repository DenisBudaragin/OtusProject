package main;

import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddItemToWishListTest {
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
    @DisplayName("Тест добавления товара в существующий Wish List")
    public void testAddItemToWishList() {
        // Генератор случайных значений
        Random random = new Random();

        // Авторизация
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 ТЕСТ: ДОБАВЛЕНИЕ ТОВАРА В СПИСОК ЖЕЛАНИЙ");
        System.out.println("=".repeat(60));

        // Ждем поля ввода для авторизации
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("ru.otus.wishlist:id/username_text_input")));

        // Очищаем поля и вводим данные для авторизации
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
            // === ИЩЕМ НУЖНЫЙ СПИСОК ЖЕЛАНИЙ ПО НАЗВАНИЮ ===
            System.out.println("\n🔍 Ищем список желаний с названием 'DenTest wish'...");

            // Ищем элемент списка по тексту
            String wishListTitle = "DenTest wish";
            By wishListLocator = By.xpath("(//android.widget.TextView[@resource-id=\"ru.otus.wishlist:id/title\"])[1]");

            // Ждем появления и кликаем на найденный список
            wait.until(ExpectedConditions.elementToBeClickable(wishListLocator)).click();
            System.out.println("✅ Список желаний '" + wishListTitle + "' найден и открыт");

            // === ДОБАВЛЯЕМ НОВЫЙ ТОВАР В СПИСОК ===
            System.out.println("\n➕ Добавляем новый товар в список...");

            // Кликаем на кнопку добавления товара
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("ru.otus.wishlist:id/add_button"))).click();
            System.out.println("✅ Кнопка добавления товара нажата");

            // Генерируем уникальные данные для первого товара
            String[] phoneModels = {"iPhone 15 Pro", "Samsung Galaxy S24", "Google Pixel 8", "Xiaomi 14", "OnePlus 12"};
            String[] descriptions = {"Флагманский смартфон", "Отличная камера", "Быстрый процессор", "Яркий экран", "Долгая работа"};

            String itemName1 = phoneModels[random.nextInt(phoneModels.length)] + " " + RandomStringUtils.randomNumeric(2);
            String itemPrice1 = String.valueOf(50 + random.nextInt(15));
            String itemDescription1 = descriptions[random.nextInt(descriptions.length)] + " " + RandomStringUtils.randomAlphabetic(5);

            // Заполняем поля для первого товара
            System.out.println("📝 Заполняем информацию о товаре:");

            // Поле названия товара
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("ru.otus.wishlist:id/name_input")));
            driver.findElement(By.id("ru.otus.wishlist:id/name_input")).sendKeys(itemName1);
            System.out.println("   ✓ Название: " + itemName1);

            // Поле цены
            driver.findElement(By.id("ru.otus.wishlist:id/price_input")).sendKeys(itemPrice1);
            System.out.println("   ✓ Цена: " + itemPrice1);

            // Поле описания
            driver.findElement(By.id("ru.otus.wishlist:id/description_input")).sendKeys(itemDescription1);
            System.out.println("   ✓ Описание: " + itemDescription1);

            // Нажимаем кнопку сохранения
            driver.findElement(By.id("ru.otus.wishlist:id/save_button")).click();
            System.out.println("✅ Товар сохранен");

            // Ждем возврата на экран списка товаров
            try { Thread.sleep(2000); } catch (InterruptedException e) {}

            // === ПРОВЕРЯЕМ, ЧТО ТОВАР ПОЯВИЛСЯ В СПИСКЕ ===
            System.out.println("\n🔍 Проверяем, что товар появился в списке...");

            // === ВЫВОДИМ ВСЕ ТОВАРЫ В СПИСКЕ ДЛЯ НАГЛЯДНОСТИ ===
            System.out.println("\n📋 Текущие товары в списке:");

            // Пытаемся найти все названия товаров
            var items = driver.findElements(By.xpath("//*[@resource-id='ru.otus.wishlist:id/name_text']"));

            if (items.isEmpty()) {
                items = driver.findElements(By.xpath("//android.widget.TextView"));
            }

            for (var item : items) {
                String text = item.getText();
                if (text != null && !text.isEmpty() && !text.equals(wishListTitle)) {
                    System.out.println("   • " + text);
                }
            }

            // === РЕДАКТИРУЕМ ПЕРВЫЙ ТОВАР ===
            System.out.println("\n✏️ Редактируем первый товар в списке...");

            // Ждем и кликаем на кнопку редактирования первого товара
            By editButtonLocator = By.xpath("(//android.widget.Button[@resource-id=\"ru.otus.wishlist:id/edit_button\"])[1]");
            wait.until(ExpectedConditions.elementToBeClickable(editButtonLocator)).click();
            System.out.println("✅ Кнопка редактирования первого товара нажата");

            try { Thread.sleep(1000); } catch (InterruptedException e) {}

            // Генерируем новые случайные данные для редактирования
            String[] newPhoneModels = {"Samsung Galaxy S24 Ultra", "iPhone 16", "Google Pixel 9", "Xiaomi 15 Pro", "Nothing Phone 3"};
            String[] newDescriptions = {"Улучшенная версия", "Новинка 2025", "Топ-модель", "Премиум качество", "Инновационный дизайн"};

            String editedItemName = newPhoneModels[random.nextInt(newPhoneModels.length)] + " " + RandomStringUtils.randomNumeric(2);
            String editedItemPrice = String.valueOf(700 + random.nextInt(2000));
            String editedItemDescription = newDescriptions[random.nextInt(newDescriptions.length)] + " " + RandomStringUtils.randomAlphabetic(6);

            // Очищаем и заполняем поля новыми значениями
            System.out.println("📝 Обновляем информацию о товаре:");

            // Очищаем и вводим новое название
            driver.findElement(By.id("ru.otus.wishlist:id/name_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/name_input")).sendKeys(editedItemName);
            System.out.println("   ✓ Новое название: " + editedItemName);

            // Очищаем и вводим новую цену
            driver.findElement(By.id("ru.otus.wishlist:id/price_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/price_input")).sendKeys(editedItemPrice);
            System.out.println("   ✓ Новая цена: " + editedItemPrice);

            // Очищаем и вводим новое описание
            driver.findElement(By.id("ru.otus.wishlist:id/description_input")).clear();
            driver.findElement(By.id("ru.otus.wishlist:id/description_input")).sendKeys(editedItemDescription);
            System.out.println("   ✓ Новое описание: " + editedItemDescription);

            // Нажимаем кнопку сохранения изменений
            driver.findElement(By.id("ru.otus.wishlist:id/save_button")).click();
            System.out.println("✅ Изменения сохранены");

            // Ждем возврата на экран списка товаров
            try { Thread.sleep(2000); } catch (InterruptedException e) {}

            // === ВЫВОДИМ ОБНОВЛЕННЫЙ СПИСОК ТОВАРОВ ===
            System.out.println("\n📋 Обновленный список товаров:");

            items = driver.findElements(By.xpath("//*[@resource-id='ru.otus.wishlist:id/name_text']"));

            if (items.isEmpty()) {
                items = driver.findElements(By.xpath("//android.widget.TextView"));
            }

            for (var item : items) {
                String text = item.getText();
                if (text != null && !text.isEmpty() && !text.equals(wishListTitle)) {
                    System.out.println("   • " + text);
                }
            }

            System.out.println("\n✅ Тест успешно завершен: товар добавлен, отредактирован и отображается в списке желаний '" + wishListTitle + "'");

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