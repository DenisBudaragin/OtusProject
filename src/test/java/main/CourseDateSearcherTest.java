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

public class CourseDateSearcherTest {
    private static final Logger log = LoggerFactory.getLogger(CourseDateSearcherTest.class);

    @Test
    public void testEarliestAndLatestCourses() throws IOException {
        List<Course> courses = Courses.parseCoursesFromPage(OTUS_COURSES_PAGE);
        List<Course> earliestСourses = Courses.getEarliestCourses(courses, DATE_FORMATTER);
        List<Course> latestСourses = Courses.getLatestCourses(courses, DATE_FORMATTER);
        CourseDataSearcherAsserts.assertNoDateOnlyCourses(earliestСourses);
        CourseDataSearcherAsserts.assertNoDateOnlyCourses(latestСourses);
    }
}

