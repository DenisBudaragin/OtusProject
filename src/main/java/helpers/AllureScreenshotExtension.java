package helpers;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureScreenshotExtension implements AfterTestExecutionCallback{
    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        if (context.getExecutionException().isPresent()) {
//            // Тест упал - делаем скриншот
//            WebDriver driver = BaseTest.getDriver();
//            if (driver != null) {
//                byte[] screenshot = AllureHelper.takeScreenshot(driver);
//                Allure.addAttachment("Скриншот после падения",
//                        new ByteArrayInputStream(screenshot));
//                Allure.addAttachment("HTML страницы",
//                        driver.getPageSource());
//            }
        }
    }
}
