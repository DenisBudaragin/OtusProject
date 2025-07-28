package main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class CourseDateSearcher {

    private static final String CATALOG_URL = "https://otus.ru/catalog/courses";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

    @Test
    public void testEarliestAndLatestCourses() throws IOException {
        // 1. Получаем список всех курсов
        List<Course> courses = parseCourses();

        assertFalse(courses.isEmpty(), "Список курсов не должен быть пустым");

        // 2. Находим самый ранний и самый поздний курсы
        List<Course> earliestDateCourses = findAllCoursesWithEarliestDate(courses);
        System.out.println("Ранние курсы:");
        earliestDateCourses.stream()
                .map(c -> String.format(c.getTitle()))
                .forEach(System.out::println);

        List<Course> coursesWithLatestDate = findAllCoursesWithLatestDate(courses);
        System.out.println("\nПоздние курсы:");
        coursesWithLatestDate.forEach(c ->
                System.out.println(c.getTitle())
        );

        // 4. Проверяем данные на страницах курсов
        assertNoDateOnlyCourses(earliestDateCourses);
        assertNoDateOnlyCourses(coursesWithLatestDate);
    }

    public static List<Course> findAllCoursesWithEarliestDate(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        // Находим минимальную дату среди всех курсов
        Optional<LocalDate> minDate = courses.stream()
                .map(Course::getStartDate)
                .map(dateStr -> dateStr.split("·")[0].trim())
                .map(dateStr -> LocalDate.parse(dateStr, DATE_FORMATTER))
                .min(LocalDate::compareTo);

        if (minDate.isEmpty()) {
            return Collections.emptyList();
        }

        // Возвращаем все курсы с минимальной датой
        return courses.stream()
                .filter(course -> {
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
                    return date.equals(minDate.get());
                })
                .toList();
    }


    public static List<Course> findAllCoursesWithLatestDate(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        // Находим максимальную дату среди всех курсов
        Optional<LocalDate> maxDate = courses.stream()
                .map(Course::getStartDate)
                .map(dateStr -> dateStr.split("·")[0].trim())
                .map(dateStr -> LocalDate.parse(dateStr, DATE_FORMATTER))
                .max(LocalDate::compareTo);

        if (maxDate.isEmpty()) {
            return Collections.emptyList();
        }

        // Возвращаем все курсы с максимальной датой
        return courses.stream()
                .filter(course -> {
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
                    return date.equals(maxDate.get());
                })
                .toList();
    }

    public static void assertNoDateOnlyCourses(List<Course> courses) {
        Pattern pattern = Pattern.compile("^(.+?[а-яА-Яa-zA-Z]{3,})\\s+\\d{1,2}\\s+[а-я]+,\\s+\\d{4}(\\s+·.*)?$");

        for (Course course : courses) {
            String courseInfo = course.getTitle();

            // Проверяем соответствие шаблону
            assertTrue(pattern.matcher(courseInfo).matches(),
                    String.format("Неверный формат курса",
                            courseInfo));

            // Дополнительная проверка длины названия
            String namePart = courseInfo.split("\\d{1,2}\\s+[а-я]+,\\s+\\d{4}")[0].trim();
            assertAll(
                    () -> assertTrue(namePart.length() >= 3,
                            "Название курса должно содержать минимум 3 символа: " + namePart),
                    () -> assertTrue(namePart.matches(".*[а-яА-Яa-zA-Z].*"),
                            "Название должно содержать буквы: " + namePart)
            );
        }
    }

    private List<Course> parseCourses() throws IOException {
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.connect(CATALOG_URL).get();
        Elements courseElements = doc.select("div.sc-18q05a6-0.incGfX > div > a");

        for (Element courseElement : courseElements) {
            String title = courseElement.text();
            String dateText = courseElement.lastElementChild().text();

            if (dateText != null) {
                courses.add(new Course(title, dateText));
            }
        }
        return courses;
    }

    static class Course {
        private final String title;
        private final String startDate;

        public Course(String title, String startDate) {
            this.title = title;
            this.startDate = startDate;
        }

        public String getTitle() {
            return title;
        }

        public String getStartDate() {
            return startDate;
        }

    }
}

