package pages;

import helpers.BaseTest;

import static configa.Config.OTUS_MAIN_PAGE;

public class MainPage extends BaseTest {
    public static void openMainPage() {
        driver.get(OTUS_MAIN_PAGE);
    }
}
