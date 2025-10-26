package main;
import asserts.RandomCategorySelectorAsserts;
import helpers.BaseTest;
import org.junit.jupiter.api.Test;
import pages.MainPage;

public class RandomCategorySelectorTest extends BaseTest {

    @Test
    public void shouldReturnCourseWithValidCategory() {
        MainPage.open();
        MainPage.clickOnCategory("Обучение");
        String actualUrl = MainPage.
                clickRandomCourseInCategory("//*[@id=\"__next\"]/div[1]/div[2]/div/nav/div[3]/div/div/div[1]/div/div/a");
        String expectedUrl = MainPage.getCurrentPageUrl();
        RandomCategorySelectorAsserts.assertUrlsAfterDelimitersEqual(expectedUrl, actualUrl);
    }
}
