package main;
import helpers.CourseDataSearcherHelper;
import org.junit.jupiter.api.Test;
import utils.Course;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static asserts.Assertions.assertNoDateOnlyCourses;
import static org.junit.jupiter.api.Assertions.*;

public class CourseDateSearcherTest {

    private static final String CATALOG_URL = "https://otus.ru/catalog/courses";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

    @Test
    public void testEarliestAndLatestCourses() throws IOException {
        // 1. Получаем список всех курсов
        CourseDataSearcherHelper courseHelper = new CourseDataSearcherHelper();
        List<Course> courses = courseHelper.parseCourses(CATALOG_URL);

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

