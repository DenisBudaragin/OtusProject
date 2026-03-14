package main;

import asserts.WishListAsserts;
import helpers.AllureHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.WishItemPage;
import pages.WishListPage;
import base.BaseTest;

@Epic("Wish List Management")
@Feature("Add Item to Wish List")
public class AddItemToWishListTest extends BaseTest {

    private static final String WISH_LIST_NAME = "ForEdited";

    @Test
    @Story("Add and edit item in existing wish list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test verifies adding and editing an item in an existing wish list")
    @DisplayName("Тест добавления товара в существующий Wish List")
    public void testAddItemToWishList() {
        Allure.step("Login to application");
        MainPage mainPage = loginPage.login(TEST_USERNAME, TEST_PASSWORD);
        Allure.step("Open wish list: " + WISH_LIST_NAME);
        WishListPage wishListPage = mainPage.openWishListByName(WISH_LIST_NAME);
        wishListPage.waitForWishListScreen();
        Allure.step("Add new item to wish list");
        WishItemPage addItemPage = wishListPage.clickAddItem();
        String itemName = randomDataGenerator.randomPhoneModel();
        String itemPrice = randomDataGenerator.randomPrice();
        String itemDescription = randomDataGenerator.randomDescription();
        AllureHelper.attachText("New item details",
                "Name: " + itemName + ", Price: " + itemPrice + ", Description: " + itemDescription);
        WishItemPage.WishListItem newItem = addItemPage.createItem(itemName, itemPrice, itemDescription);
        Allure.step("Edit the first item");
        WishItemPage editItemPage = wishListPage.clickEditButtonForItem(0);
        String editedItemName = randomDataGenerator.randomNewPhoneModel();
        String editedItemPrice = randomDataGenerator.randomPrice();
        String editedItemDescription = randomDataGenerator.randomNewDescription();
        AllureHelper.attachText("Edited item details",
                "Name: " + editedItemName + ", Price: " + editedItemPrice + ", Description: " + editedItemDescription);
        editItemPage.editItem(editedItemName, editedItemPrice, editedItemDescription);
        Allure.step("Verify item was edited");
        WishListAsserts.assertItemEdited(wishListPage, itemName, editedItemName);
        wishListPage.printAllItems();
        AllureHelper.attachText("Test result", "✅ Тест успешно завершен: товар добавлен и отредактирован");
    }
}