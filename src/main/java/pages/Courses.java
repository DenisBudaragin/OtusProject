package pages;
import helpers.CourseDataSearcherHelper;
import utils.Course;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Courses {
    public static final String RESET = "\033[0m";
    public static final String GREEN = "\033[92m";
    public static final String RED = "\033[91m";

    public static List<Course> parseCoursesFromPage(String pageUrl) throws IOException {
        List<Course> courses = CourseDataSearcherHelper.parseCourses(pageUrl, element -> {
            String title = element.text();
            String dateText = element.lastElementChild().text();
            return new Course(title, dateText);
        });
        assertFalse(courses.isEmpty(), "Список курсов не должен быть пустым");
        return courses;
    }

    public static List<Course> getEarliestCourses(List<Course> courses, DateTimeFormatter dateFormatter) {
        List<Course> earliestDateCourses = CourseDataSearcherHelper.findAllCoursesWithEarliestDate(courses, dateFormatter);
        System.out.println(GREEN + "Ранние курсы:" + RESET);
        earliestDateCourses.stream()
                .map(c -> String.format(c.getTitle()))
                .forEach(System.out::println);
        return earliestDateCourses;
    }

    public static List<Course> getLatestCourses(List<Course> courses, DateTimeFormatter dateFormatter) {
        List<Course> coursesWithLatestDate = CourseDataSearcherHelper.findAllCoursesWithLatestDate(courses, dateFormatter);
        System.out.println(RED + "\nПоздние курсы:" + RESET);
        coursesWithLatestDate.forEach(c ->
                System.out.println(c.getTitle())
        );
        return coursesWithLatestDate;
    }
}
