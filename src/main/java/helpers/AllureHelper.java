package helpers;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AllureHelper {

    public static void step(String stepName) {
        Allure.step(stepName);
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

//    public static void attachScreenshot(AndroidDriver driver, String name) {
//        try {
//            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//            Allure.addAttachment(name + " - " + getCurrentTimestamp(),
//                    new ByteArrayInputStream(screenshot));
//        } catch (Exception e) {
//            attachText("Screenshot failed for: " + name, e.getMessage());
//        }
//    }

    public static String attachPageSource(AndroidDriver driver) {
        String pageSource = driver.getPageSource();
        Allure.addAttachment("Page Source", "text/xml", pageSource);
        return pageSource;
    }

    public static void attachEnvironmentInfo() {
        Allure.addAttachment("Environment", "text/plain",
                "Platform: Android 13\nDevice: redroid13\nApp: ru.otus.wishlist");
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}