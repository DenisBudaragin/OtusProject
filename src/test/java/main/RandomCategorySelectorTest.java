package main;
import asserts.RandomCategorySelectorAsserts;
import helpers.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.MainPage;

@Epic("Управление курсами")
@Feature("Случайный выбор курса")
@Story("Выбор случайного курса из категории")
public class RandomCategorySelectorTest extends BaseTest {
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет, что при выборе случайного курса из категории URL корректен")
    @DisplayName("Выбор случайного курса из категории Обучение")
    @Tag("smoke")
    @Tag("regression")
    @TmsLink("TC-123")
    @Issue("BUG-456")
    @Link(name = "Documentation", url = "https://example.com/docs")
    public void shouldReturnCourseWithValidCategory() {
        Allure.step("Открыть главную страницу");
        MainPage.open();

        Allure.step("Кликнуть на категорию Обучение");
        MainPage.clickOnCategory("Обучение");

        Allure.step("Выбрать случайный курс в категории");
        String actualUrl = MainPage.
                clickRandomCourseInCategory("//*[@id=\"__next\"]/div[1]/div[2]/div/nav/div[3]/div/div/div[1]/div/div/a");
        String expectedUrl = MainPage.getCurrentPageUrl();

        Allure.step("Проверить соответствие URL");
        RandomCategorySelectorAsserts.assertUrlsAfterDelimitersEqual(expectedUrl, actualUrl);
    }
}
