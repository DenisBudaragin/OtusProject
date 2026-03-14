package helpers;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ElementActions {
    private final AndroidDriver driver;
    private final WaitHelper wait;

    public ElementActions(AndroidDriver driver, WaitHelper wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void click(By locator) {
        wait.forElementToBeClickable(locator).click();
    }

    public void sendKeys(By locator, String text) {
        wait.forPresenceOfElement(locator).sendKeys(text);
    }

    public void clearAndSendKeys(By locator, String text) {
        var element = wait.forPresenceOfElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return wait.forPresenceOfElement(locator).getText();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAttribute(By locator, String attribute) {
        return wait.forPresenceOfElement(locator).getAttribute(attribute);
    }
}