package pages;

import helpers.AllureHelper;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class UsersPage extends BasePage {
    private static final By USERS_TAB = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"ru.otus.wishlist:id/navigation_bar_item_icon_view\").instance(1)");
    private static final By FILTER_BUTTON = AppiumBy.id("ru.otus.wishlist:id/filter");
    private static final By USERNAME_INPUT = AppiumBy.id("ru.otus.wishlist:id/username_input");
    private static final By APPLY_BUTTON = AppiumBy.id("ru.otus.wishlist:id/apply_button");
    private static final By USERNAME = AppiumBy.id("ru.otus.wishlist:id/username");

    public UsersPage(AndroidDriver driver) {
        super(driver);
    }

    public void navigateToUsersTab() {
        AllureHelper.step("Navigating to users tab");
        wait.forElementToBeClickable(USERS_TAB).click();
        sleep(2000);
    }

    public void openFilter() {
        AllureHelper.step("Opening filter");
        wait.forElementToBeClickable(FILTER_BUTTON).click();
        sleep(1000);
    }

    public void filterByUsername(String username) {
        AllureHelper.step("Filtering by username: " + username);
        wait.forPresenceOfElement(USERNAME_INPUT);
        elementActions.clearAndSendKeys(USERNAME_INPUT, username);
        AllureHelper.attachText("Filter username", username);
        sleep(500);
    }

    public void applyFilter() {
        AllureHelper.step("Applying filter");
        wait.forElementToBeClickable(APPLY_BUTTON).click();
        sleep(2000);
    }

    public WishListPage selectFirstUser() {
        AllureHelper.step("Selecting first user from list");
        wait.forElementToBeClickable(USERNAME).click();
        sleep(2000);
        return new WishListPage(driver);
    }
}