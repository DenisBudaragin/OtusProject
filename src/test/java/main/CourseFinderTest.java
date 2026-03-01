package main;
import asserts.CourseFinderAsserts;
import helpers.AllureHelper;
import helpers.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CoursesPage;
import static configa.Config.*;
import io.qameta.allure.Step;

@Epic("Управление курсами")
@Feature("Поиск курсов")
public class CourseFinderTest extends BaseTest {
    @Test
    @Story("Поиск конкретного курса")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет возможность поиска и открытия конкретного курса")
    public void testCourseFinder() {
        Allure.step("Открыть страницу с курсами");
        CoursesPage.open();
        AllureHelper.attachText("URL страницы", driver.getCurrentUrl());

        Allure.step("Найти и кликнуть на курс: " + COURSE_NAME);
        CoursesPage.findAndClickCourseByName(COURSE_NAME, COURSE_XPATH);
        AllureHelper.attachText("Название найденного курса", COURSE_NAME);

        Allure.step("Проверить соответствие названия курса");
        CourseFinderAsserts.assertCourseNameMatches(driver, COURSE_NAME);
        AllureHelper.attachText("Результат проверки", "Название курса соответствует ожидаемому");
    }
}

