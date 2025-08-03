package helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Course;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CourseDataSearcherHelper {
    public List<Course> parseCourses(String url) throws IOException {
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
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
    public List<Course> findAllCoursesWithEarliestDate(List<Course> courses, DateTimeFormatter dateFormat) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        // Находим минимальную дату среди всех курсов
        Optional<LocalDate> minDate = courses.stream()
                .map(Course::getStartDate)
                .map(dateStr -> dateStr.split("·")[0].trim())
                .map(dateStr -> LocalDate.parse(dateStr, dateFormat))
                .min(LocalDate::compareTo);

        if (minDate.isEmpty()) {
            return Collections.emptyList();
        }

        // Возвращаем все курсы с минимальной датой
        return courses.stream()
                .filter(course -> {
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, dateFormat);
                    return date.equals(minDate.get());
                })
                .toList();
    }


    public List<Course> findAllCoursesWithLatestDate(List<Course> courses, DateTimeFormatter dateFormat) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        // Находим максимальную дату среди всех курсов
        Optional<LocalDate> maxDate = courses.stream()
                .map(Course::getStartDate)
                .map(dateStr -> dateStr.split("·")[0].trim())
                .map(dateStr -> LocalDate.parse(dateStr, dateFormat))
                .max(LocalDate::compareTo);

        if (maxDate.isEmpty()) {
            return Collections.emptyList();
        }

        // Возвращаем все курсы с максимальной датой
        return courses.stream()
                .filter(course -> {
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, dateFormat);
                    return date.equals(maxDate.get());
                })
                .toList();
    }


}
