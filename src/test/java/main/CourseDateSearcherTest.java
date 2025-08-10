package main;
import helpers.CourseDataSearcherHelper;
import org.junit.jupiter.api.Test;
import utils.Course;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static asserts.Assertions.assertNoDateOnlyCourses;
import static helpers.Config.*;
import static org.junit.jupiter.api.Assertions.*;

public class CourseDateSearcherTest {
    @Test
    public void testEarliestAndLatestCourses() throws IOException {
        // 1. Получаем список всех курсов
        CourseDataSearcherHelper courseHelper = new CourseDataSearcherHelper();

        List<Course> courses = courseHelper.parseCourses("https://otus.ru/catalog/courses", element -> {
            String title = element.text();
            String dateText = element.lastElementChild().text();
            return new Course(title, dateText);
        });

        assertFalse(courses.isEmpty(), "Список курсов не должен быть пустым");

        // 2. Находим самый ранний и самый поздний курсы
        List<Course> earliestDateCourses = courseHelper.findAllCoursesWithEarliestDate(courses, DATE_FORMATTER);
        System.out.println("Ранние курсы:");
        earliestDateCourses.stream()
                .map(c -> String.format(c.getTitle()))
                .forEach(System.out::println);

        List<Course> coursesWithLatestDate = courseHelper.findAllCoursesWithLatestDate(courses, DATE_FORMATTER);
        System.out.println("\nПоздние курсы:");
        coursesWithLatestDate.forEach(c ->
                System.out.println(c.getTitle())
        );

        // 4. Проверяем данные на страницах курсов
        assertNoDateOnlyCourses(earliestDateCourses);
        assertNoDateOnlyCourses(coursesWithLatestDate);
    }
}

