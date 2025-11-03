package stepdefinitions;

import asserts.CourseDataSearcherAsserts;
import asserts.CourseFinderAsserts;
import com.google.inject.Inject;
import configa.Config;
import helpers.BaseTest;
import helpers.CourseDataSearcherHelper;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.openqa.selenium.WebDriver;
import pages.Courses;
import pages.CoursesPage;
import utils.Course;

import java.io.IOException;
import java.util.List;

import static configa.Config.*;

public class CoursePageSteps extends BaseTest {
    @Пусть("Я открываю браузер Chrome")
    public void openChromeBrowser() {
        CoursesPage.open();
    }

    @И("Найти курс с названием {string} и путём до элемента в дереве элементов {string}")
    public void searchCourseByName(String courseName, String courseXpath) {
        CoursesPage.findAndClickCourseByName(courseName, courseXpath);
    }

    @Тогда("Проверить что открыта страница курса {string}")
    public void checkWebPage(String courseName) {
        CourseFinderAsserts.assertCourseNameMatches(driver, courseName);
    }

    @И("Найти курс в указанную дату {string} или позже указанной даты")
    public void findCourse(String courseDate) throws IOException {
        List<Course> courses = Courses.parseCoursesFromPage(OTUS_COURSES_PAGE);
        List<Course> filteredCourses = CourseDataSearcherHelper.findCoursesOnOrAfterDate(courses, courseDate, DATE_FORMATTER);

        String PURPLE = "\033[95m";
        String RESET = "\033[0m";
        String GREEN = "\033[92m";

        if (filteredCourses.isEmpty()) {
            CourseDataSearcherAsserts.assertFilteredCourses(filteredCourses, courseDate);
        } else {
            System.out.println(GREEN + "Найдено " + filteredCourses.size() + " курсов на дату " + courseDate + " или позже:" + RESET);
            filteredCourses.forEach(course ->
                    System.out.println(PURPLE +
                            String.format("- %s (%s)",
                                    course.getTitle(),
                                    course.getStartDate()) +
                            RESET)
            );
        }
    }
}
