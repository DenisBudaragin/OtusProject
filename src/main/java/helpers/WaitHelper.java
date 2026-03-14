package helpers;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelper {
    private final WebDriverWait wait;
    private final AndroidDriver driver;

    public WaitHelper(AndroidDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public WebElement forPresenceOfElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public WebElement forElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement forVisibilityOfElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement forPresenceOfElement(By locator, long timeoutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return customWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public boolean forElementToDisappear(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}