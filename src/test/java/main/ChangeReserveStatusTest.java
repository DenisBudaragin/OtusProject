package main;

import asserts.WishItemAsserts;
import helpers.AllureHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.UsersPage;
import pages.WishListPage;
import base.BaseTest;

@Epic("Wish List Management")
@Feature("Reservation Status")
public class ChangeReserveStatusTest extends BaseTest {

    private static final String TARGET_USERNAME = "tonyp90";
    private static final String WISH_LIST_NAME = "Ждем следующий год";

    @Test
    @Story("Change reservation status of another user's gift")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test verifies changing reservation status of another user's gift")
    @DisplayName("Тест изменения статуса резервирования подарка другого пользователя")
    public void testChangeReservationStatus() {
        Allure.step("Login to application");
        MainPage mainPage = loginPage.login(TEST_USERNAME, TEST_PASSWORD);
        Allure.step("Navigate to users tab");
        UsersPage usersPage = new UsersPage(driver);
        usersPage.navigateToUsersTab();
        Allure.step("Filter user: " + TARGET_USERNAME);
        usersPage.openFilter();
        usersPage.filterByUsername(TARGET_USERNAME);
        usersPage.applyFilter();
        Allure.step("Select user: " + TARGET_USERNAME);
        WishListPage userWishListPage = usersPage.selectFirstUser();
        Allure.step("Select wish list: " + WISH_LIST_NAME);
        WishListPage wishListPage = userWishListPage.selectWishListByName(WISH_LIST_NAME);
        Allure.step("Check current reservation status");
        String oldStatus = wishListPage.getReservationStatus(0);
        AllureHelper.attachText("Current status", oldStatus.equals("true") ? "RESERVED" : "NOT RESERVED");
        Allure.step("Toggle reservation status");
        String newStatus = wishListPage.toggleReservationStatus(0);
        Allure.step("Verify status changed");
        WishItemAsserts.assertStatusChanged(oldStatus, newStatus);
        Allure.step("Print all items with their statuses");
        wishListPage.printAllItems();
        AllureHelper.attachText("Test result", "✅ ТЕСТ УСПЕШНО ЗАВЕРШЕН: Статус резервирования изменен");
    }
}