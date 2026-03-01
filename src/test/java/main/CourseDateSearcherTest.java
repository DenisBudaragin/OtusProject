package main;
import asserts.CourseDataSearcherAsserts;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.Courses;
import utils.Course;
import java.io.IOException;
import java.util.*;
import static configa.Config.*;
import helpers.AllureHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Epic("Управление курсами")
@Feature("Поиск курсов по датам")
public class CourseDateSearcherTest {
    private static final Logger log = LoggerFactory.getLogger(CourseDateSearcherTest.class);

    @Test
    @Story("Поиск самых ранних и поздних курсов")
    @DisplayName("Проверка форматирования дат курсов")
    @Severity(SeverityLevel.NORMAL)
    @Tag("courses")
    @Description("Тест проверяет корректность форматирования дат для самых ранних и поздних курсов")
    public void testEarliestAndLatestCourses() throws IOException {
        Allure.step("Парсинг страницы с курсами");
        List<Course> courses = Courses.parseCoursesFromPage(OTUS_COURSES_PAGE);
        AllureHelper.attachText("Количество курсов", "Найдено курсов: " + courses.size());

        Allure.step("Получение самых ранних курсов");
        List<Course> earliestСourses = Courses.getEarliestCourses(courses, DATE_FORMATTER);

        Allure.step("Получение самых поздних курсов");
        List<Course> latestСourses = Courses.getLatestCourses(courses, DATE_FORMATTER);

        Allure.step("Проверка форматирования ранних курсов");
        CourseDataSearcherAsserts.assertNoDateOnlyCourses(earliestСourses);

        Allure.step("Проверка форматирования поздних курсов");
        CourseDataSearcherAsserts.assertNoDateOnlyCourses(latestСourses);

        AllureHelper.attachText("Результат",
                String.format("Ранних курсов: %d, Поздних курсов: %d",
                        earliestСourses.size(), latestСourses.size()));
    }
}

