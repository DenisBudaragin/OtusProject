package pages;

import helpers.AllureHelper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class WishItemPage extends BasePage {
    private static final By NAME_INPUT = By.id("ru.otus.wishlist:id/name_input");
    private static final By PRICE_INPUT = By.id("ru.otus.wishlist:id/price_input");
    private static final By DESCRIPTION_INPUT = By.id("ru.otus.wishlist:id/description_input");
    private static final By TITLE_INPUT = By.id("ru.otus.wishlist:id/title_input");
    private static final By SAVE_BUTTON = By.id("ru.otus.wishlist:id/save_button");

    public WishItemPage(AndroidDriver driver) {
        super(driver);
    }

    public void waitForItemForm() {
        wait.forPresenceOfElement(NAME_INPUT);
    }

    public void enterItemName(String name) {
        elementActions.clearAndSendKeys(NAME_INPUT, name);
        AllureHelper.attachText("Item name", name);
    }

    public void enterItemPrice(String price) {
        elementActions.clearAndSendKeys(PRICE_INPUT, price);
        AllureHelper.attachText("Item price", price);
    }

    public void enterItemDescription(String description) {
        elementActions.clearAndSendKeys(DESCRIPTION_INPUT, description);
        AllureHelper.attachText("Item description", description);
    }

    public void enterWishListTitle(String title) {
        elementActions.clearAndSendKeys(TITLE_INPUT, title);
        AllureHelper.attachText("Wish list title", title);
    }

    public void enterWishListDescription(String description) {
        elementActions.clearAndSendKeys(DESCRIPTION_INPUT, description);
        AllureHelper.attachText("Wish list description", description);
    }

    public void clickSave() {
        elementActions.click(SAVE_BUTTON);
        AllureHelper.attachText("Save clicked", "Saving changes");
        sleep(2000);
    }

    public WishListItem createItem(String name, String price, String description) {
        AllureHelper.step("Creating item: " + name);
        waitForItemForm();
        enterItemName(name);
        enterItemPrice(price);
        enterItemDescription(description);
        clickSave();
        return new WishListItem(name, price, description);
    }

    public WishListItem editItem(String name, String price, String description) {
        AllureHelper.step("Editing item to: " + name);
        waitForItemForm();
        enterItemName(name);
        enterItemPrice(price);
        enterItemDescription(description);
        clickSave();
        return new WishListItem(name, price, description);
    }

    public static class WishListItem {
        public final String name;
        public final String price;
        public final String description;

        public WishListItem(String name, String price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }
    }
}