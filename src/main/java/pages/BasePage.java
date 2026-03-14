package pages;

import com.google.inject.Inject;
import helpers.ElementActions;
import helpers.WaitHelper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    @Inject
    protected AndroidDriver driver;

    protected WaitHelper wait;
    protected WaitHelper longWait;
    protected ElementActions elementActions;

    public BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver, Duration.ofSeconds(30));
        this.longWait = new WaitHelper(driver, Duration.ofSeconds(60));
        this.elementActions = new ElementActions(driver, wait);
    }

    public String getCurrentActivity() {
        return driver.currentActivity();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}