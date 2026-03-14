package pages;

import helpers.AllureHelper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class MainPage extends BasePage {
    private static final By ADD_BUTTON = By.id("ru.otus.wishlist:id/add_button");
    private static final By WISH_LIST_TITLE = By.id("ru.otus.wishlist:id/title");
    private static final By WISH_LIST_TITLE_TEXT = By.id("ru.otus.wishlist:id/title_text");

    public MainPage(AndroidDriver driver) {
        super(driver);
    }

    public void waitForMainScreen() {
        wait.forElementToBeClickable(ADD_BUTTON);
//        AllureHelper.attachScreenshot(driver, "Main screen loaded");
    }

    public void clickAddButton() {
        elementActions.click(ADD_BUTTON);
        AllureHelper.attachText("Clicked add button", "Creating new wish list/item");
    }

    public WishListPage openWishListByName(String wishListName) {
        AllureHelper.step("Opening wish list: " + wishListName);
        By wishListLocator = By.xpath("//android.widget.TextView[@resource-id='ru.otus.wishlist:id/title' and @text='" + wishListName + "']");
        wait.forElementToBeClickable(wishListLocator).click();
//        AllureHelper.attachScreenshot(driver, "Wish list opened");
        return new WishListPage(driver);
    }

    public WishListPage openFirstWishList() {
        AllureHelper.step("Opening first wish list");
        By firstWishListLocator = By.xpath("(//android.widget.TextView[@resource-id='ru.otus.wishlist:id/title'])[1]");
        wait.forElementToBeClickable(firstWishListLocator).click();
//        AllureHelper.attachScreenshot(driver, "First wish list opened");
        return new WishListPage(driver);
    }

    public boolean isWishListDisplayed(String wishListName) {
        return elementActions.isElementDisplayed(By.xpath("//*[@text='" + wishListName + "']"));
    }

    public void printAllWishLists() {
        AllureHelper.step("Printing all wish lists");
        var elements = driver.findElements(WISH_LIST_TITLE_TEXT);
        for (var element : elements) {
            String text = element.getText();
            if (text != null && !text.isEmpty()) {
                AllureHelper.attachText("Wish list found", text);
            }
        }
    }
}