package pages;

import helpers.AllureHelper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class ProfilePage extends BasePage {
    private static final By PROFILE_TAB = By.xpath("(//android.widget.ImageView[@resource-id='ru.otus.wishlist:id/navigation_bar_item_icon_view'])[3]");
    private static final By PROFILE_TAB_ALT = By.xpath("//android.widget.FrameLayout[@content-desc='Profile']");
    private static final By LOGOUT_BUTTON = By.id("ru.otus.wishlist:id/log_out_button");
    private static final By CONFIRM_LOGOUT_BUTTON = By.id("android:id/button1");

    public ProfilePage(AndroidDriver driver) {
        super(driver);
    }

    public void navigateToProfile() {
        AllureHelper.step("Navigating to profile tab");
        try {
            wait.forElementToBeClickable(PROFILE_TAB).click();
            AllureHelper.attachText("Profile navigation", "Clicked profile tab via XPath");
        } catch (Exception e) {
            AllureHelper.attachText("Profile navigation", "Trying alternative locator");
            wait.forElementToBeClickable(PROFILE_TAB_ALT).click();
        }
        sleep(1000);
//        AllureHelper.attachScreenshot(driver, "Profile screen");
    }

    public void logout() {
        AllureHelper.step("Logging out");
        wait.forElementToBeClickable(LOGOUT_BUTTON).click();
        AllureHelper.attachText("Logout", "Clicked logout button");
        sleep(1000);

        try {
            if (driver.findElements(CONFIRM_LOGOUT_BUTTON).size() > 0) {
                driver.findElement(CONFIRM_LOGOUT_BUTTON).click();
                AllureHelper.attachText("Logout", "Confirmed logout");
                sleep(1000);
            }
        } catch (Exception e) {
            AllureHelper.attachText("Logout", "No confirmation dialog");
        }
    }

    public boolean isLoggedOut() {
        return wait.forPresenceOfElement(By.id("ru.otus.wishlist:id/username_text_input")) != null;
    }
}