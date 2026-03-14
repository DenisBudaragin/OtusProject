package asserts;

import helpers.AllureHelper;
import pages.MainPage;
import pages.WishListPage;
import org.junit.jupiter.api.Assertions;

public class WishListAsserts {

    public static void assertWishListCreated(MainPage mainPage, String wishListName) {
        boolean isDisplayed = mainPage.isWishListDisplayed(wishListName);
        AllureHelper.attachText("Wish list created check",
                "Expected: " + wishListName + ", Found: " + isDisplayed);
        Assertions.assertTrue(isDisplayed,
                "Wish list '" + wishListName + "' should be displayed after creation");
    }

    public static void assertWishListEdited(MainPage mainPage, String oldTitle, String newTitle) {
        boolean oldNotFound = !mainPage.isWishListDisplayed(oldTitle);
        boolean newFound = mainPage.isWishListDisplayed(newTitle);

        AllureHelper.attachText("Wish list edit check",
                "Old title not displayed: " + oldNotFound + ", New title displayed: " + newFound);

        Assertions.assertTrue(oldNotFound,
                "Old wish list title '" + oldTitle + "' should not be displayed after editing");
        Assertions.assertTrue(newFound,
                "New wish list title '" + newTitle + "' should be displayed after editing");
    }

    public static void assertItemAdded(WishListPage wishListPage, String itemName) {
        boolean isDisplayed = wishListPage.isItemDisplayed(itemName);
        AllureHelper.attachText("Item added check",
                "Item '" + itemName + "' displayed: " + isDisplayed);
        Assertions.assertTrue(isDisplayed,
                "Item '" + itemName + "' should be displayed after adding");
    }

    public static void assertItemEdited(WishListPage wishListPage, String oldName, String newName) {
        boolean oldNotFound = !wishListPage.isItemDisplayed(oldName);
        boolean newFound = wishListPage.isItemDisplayed(newName);

        AllureHelper.attachText("Item edit check",
                "Old name not displayed: " + oldNotFound + ", New name displayed: " + newFound);

        Assertions.assertTrue(oldNotFound,
                "Old item name '" + oldName + "' should not be displayed after editing");
        Assertions.assertTrue(newFound,
                "New item name '" + newName + "' should be displayed after editing");
    }
}