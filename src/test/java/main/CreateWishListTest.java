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
@Feature("Create Wish List")
public class CreateWishListTest extends BaseTest {

    @Test
    @Story("Create and edit wish list")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Test verifies creating and editing a wish list")
    @DisplayName("Тест успешной авторизации, создания и редактирования списка желаний")
    public void testCreateWishList() {
        Allure.step("Login to application");
        MainPage mainPage = loginPage.login(TEST_USERNAME, TEST_PASSWORD);
        Allure.step("Create new wish list");
        mainPage.clickAddButton();
        WishItemPage wishItemPage = new WishItemPage(driver);
        String wishListTitle = randomDataGenerator.randomWishListTitle();
        String wishListDescription = "Autotest 15 Pro Max, 256GB, черный";
        AllureHelper.attachText("New wish list details",
                "Title: " + wishListTitle + ", Description: " + wishListDescription);
        wishItemPage.enterWishListTitle(wishListTitle);
        wishItemPage.enterWishListDescription(wishListDescription);
        wishItemPage.clickSave();
        Allure.step("Verify wish list was created");
        WishListAsserts.assertWishListCreated(mainPage, wishListTitle);
        mainPage.printAllWishLists();
        Allure.step("Edit the created wish list");
        WishListPage wishListPage = mainPage.openFirstWishList();
        String editedTitle = randomDataGenerator.randomEditedWishListTitle();
        AllureHelper.attachText("Edited title", editedTitle);
        WishItemPage editPage = wishListPage.clickEditButtonForItem(1);
        editPage.enterWishListTitle(editedTitle);
        editPage.clickSave();
        Allure.step("Verify wish list was edited");
        WishListAsserts.assertWishListEdited(mainPage, wishListTitle, editedTitle);
        mainPage.printAllWishLists();
        AllureHelper.attachText("Test result", "✅ Тест успешно завершен: пункт добавлен и отредактирован");
    }
}