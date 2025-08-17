package pages;

import helpers.BaseTest;

import static helpers.Config.OTUS_COURSES_PAGE;

public class CoursesPage extends BaseTest {
    public static void openCoursesPage() {
        driver.get(OTUS_COURSES_PAGE);
    }
}
