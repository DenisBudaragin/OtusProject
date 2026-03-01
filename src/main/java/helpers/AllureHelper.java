package helpers;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureHelper {
    private static final Logger log = LoggerFactory.getLogger(AllureHelper.class);

    @Attachment(value = "Скриншот при падении теста", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        if (driver == null) {
            log.warn("WebDriver is null, cannot take screenshot");
            return new byte[0];
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String name, String content) {
        return content;
    }

    @Attachment(value = "HTML страницы", type = "text/html")
    public static String attachPageSource(WebDriver driver) {
        if (driver == null) return "";
        return driver.getPageSource();
    }
}
