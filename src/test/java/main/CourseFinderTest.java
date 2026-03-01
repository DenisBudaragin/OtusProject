package main;
import asserts.CourseFinderAsserts;
import helpers.BaseTest;
import org.junit.jupiter.api.Test;
import pages.CoursesPage;
import static configa.Config.*;

public class CourseFinderTest extends BaseTest {
    @Test
    public void testCourseFinder() {
        CoursesPage.open();
        CoursesPage.findAndClickCourseByName(COURSE_NAME, COURSE_XPATH);
        CourseFinderAsserts.assertCourseNameMatches(driver, COURSE_NAME);
    }
}

