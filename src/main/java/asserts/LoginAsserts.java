package asserts;

import helpers.AllureHelper;
import pages.LoginPage;
import org.junit.jupiter.api.Assertions;

public class LoginAsserts {

    public static void assertLoginSuccessful(LoginPage loginPage, String username) {
        boolean isLoginScreenGone = !loginPage.isLoginScreenDisplayed();
        AllureHelper.attachText("Login success check",
                "User: " + username + ", Login screen hidden: " + isLoginScreenGone);
        Assertions.assertTrue(isLoginScreenGone,
                "Login screen should disappear after successful login for user: " + username);
    }

    public static void assertLogoutSuccessful(LoginPage loginPage) {
        boolean isLoginScreenDisplayed = loginPage.isLoginScreenDisplayed();
        AllureHelper.attachText("Logout check",
                "Login screen displayed after logout: " + isLoginScreenDisplayed);
        Assertions.assertTrue(isLoginScreenDisplayed,
                "Login screen should be displayed after logout");
    }
}