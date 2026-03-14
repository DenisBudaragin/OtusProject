package pages;

import helpers.AllureHelper;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private static final By USERNAME_INPUT = By.id("ru.otus.wishlist:id/username_text_input");
    private static final By PASSWORD_INPUT = By.id("ru.otus.wishlist:id/password_text_input");
    private static final By LOGIN_BUTTON = By.id("ru.otus.wishlist:id/log_in_button");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    public void waitForLoginScreen() {
        wait.forPresenceOfElement(USERNAME_INPUT);
    }

    public void enterUsername(String username) {
        elementActions.clearAndSendKeys(USERNAME_INPUT, username);
        AllureHelper.attachText("Entered username", username);
    }

    public void enterPassword(String password) {
        elementActions.clearAndSendKeys(PASSWORD_INPUT, password);
        AllureHelper.attachText("Entered password", password);
    }

    public void clickLoginButton() {
        elementActions.click(LOGIN_BUTTON);
        AllureHelper.attachText("Clicked login button", "Login attempt");
    }

    public MainPage login(String username, String password) {
        AllureHelper.step("Login with username: " + username);
        waitForLoginScreen();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        sleep(3000);
        return new MainPage(driver);
    }

    public boolean isLoginScreenDisplayed() {
        return elementActions.isElementDisplayed(USERNAME_INPUT);
    }
}