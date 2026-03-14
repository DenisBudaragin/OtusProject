package pages;

import helpers.AllureHelper;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class WishListPage extends BasePage {
    private static final By ADD_BUTTON = By.id("ru.otus.wishlist:id/add_button");
    private static final By ITEM_NAME_TEXT = By.id("ru.otus.wishlist:id/name_text");
    private static final By RESERVED_SWITCH = AppiumBy.androidUIAutomator(
            "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\")");
    private static final By EDIT_BUTTON = By.id("ru.otus.wishlist:id/edit_button");

    public WishListPage(AndroidDriver driver) {
        super(driver);
    }

    public void waitForWishListScreen() {
        wait.forElementToBeClickable(ADD_BUTTON);
    }

    public WishItemPage clickAddItem() {
        AllureHelper.step("Adding new item to wish list");
        wait.forElementToBeClickable(ADD_BUTTON).click();
        return new WishItemPage(driver);
    }

    public WishItemPage clickEditButtonForItem(int index) {
        AllureHelper.step("Editing item at index: " + index);
        By editButtonLocator = By.xpath("(//android.widget.Button[@resource-id='ru.otus.wishlist:id/edit_button'])[" + (index + 1) + "]");
        wait.forElementToBeClickable(editButtonLocator).click();
        sleep(1000);
        return new WishItemPage(driver);
    }

    public String toggleReservationStatus(int itemIndex) {
        AllureHelper.step("Toggling reservation status for item: " + itemIndex);
        By switchLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\").instance(" + itemIndex + ")");

        String oldStatus = driver.findElement(switchLocator).getAttribute("checked");
        AllureHelper.attachText("Old status", oldStatus.equals("true") ? "RESERVED" : "NOT RESERVED");

        wait.forElementToBeClickable(switchLocator).click();
        sleep(1000);

        String newStatus = driver.findElement(switchLocator).getAttribute("checked");
        AllureHelper.attachText("New status", newStatus.equals("true") ? "RESERVED" : "NOT RESERVED");
        return newStatus;
    }

    public String getReservationStatus(int itemIndex) {
        By switchLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\").instance(" + itemIndex + ")");
        return driver.findElement(switchLocator).getAttribute("checked");
    }

    public void printAllItems() {
        AllureHelper.step("Printing all items");
        var items = driver.findElements(ITEM_NAME_TEXT);

        for (int i = 0; i < items.size(); i++) {
            try {
                String itemName = items.get(i).getText();
                By switchForItem = AppiumBy.androidUIAutomator(
                        "new UiSelector().resourceId(\"ru.otus.wishlist:id/reserved\").instance(" + i + ")");
                String status = driver.findElement(switchForItem).getAttribute("checked");
                String statusText = status.equals("true") ? "RESERVED" : "NOT RESERVED";
                AllureHelper.attachText("Item " + i, itemName + " - " + statusText);
            } catch (Exception e) {
                AllureHelper.attachText("Item " + i, "Could not get status");
            }
        }
    }

    public boolean isItemDisplayed(String itemName) {
        return elementActions.isElementDisplayed(By.xpath("//*[@text='" + itemName + "']"));
    }

    public WishListPage selectWishListByName(String wishListName) {
        AllureHelper.step("Selecting wish list: " + wishListName);
        By wishlistLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + wishListName + "\")");
        wait.forElementToBeClickable(wishlistLocator).click();
        sleep(2000);
        return this;
    }
}