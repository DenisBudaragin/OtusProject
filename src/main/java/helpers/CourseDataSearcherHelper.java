package helpers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Course;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CourseDataSearcherHelper {
    public static <T extends Course> List<T> parseCourses(String url, Function<Element, T> courseCreator) throws IOException {
        List<T> courses = new ArrayList<>();

        Connection connection = Jsoup.connect(url)
                .timeout(60000) // 60 секунд
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .maxBodySize(0) // без ограничения размера
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        try {
            Document doc = connection.get();
            Elements courseElements = doc.select("div.sc-18q05a6-0.incGfX > div > a");

            for (Element courseElement : courseElements) {
                T course = courseCreator.apply(courseElement);
                courses.add(course);
            }
        } catch (SSLException e) {
            // Повторная попытка без проверки SSL
            System.out.println("SSL error, retrying without certificate validation...");
            try {
                Document doc = connection.followRedirects(false).get();
                Elements courseElements = doc.select("div.sc-18q05a6-0.incGfX > div > a");

                for (Element courseElement : courseElements) {
                    T course = courseCreator.apply(courseElement);
                    courses.add(course);
                }
            } catch (Exception ex) {
                throw new IOException("Failed to parse courses after SSL retry", ex);
            }
        }

        return courses;
    }


    public static List<Course> findAllCoursesWithEarliestDate(List<Course> courses, DateTimeFormatter dateFormat) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        return courses.stream()
                .map(course -> {
                    // Парсим дату из строки курса
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, dateFormat);
                    // Создаем пару курс-дата
                    return new AbstractMap.SimpleEntry<>(course, date);
                })
                .reduce(
                        // Начальное значение аккумулятора - пустой список и максимально возможная дата
                        new AbstractMap.SimpleEntry<List<Course>, LocalDate>(new ArrayList<>(), LocalDate.MAX),

                        // Функция аккумуляции
                        (acc, entry) -> {
                            LocalDate currentDate = entry.getValue();
                            LocalDate minDate = acc.getValue();

                            if (currentDate.isBefore(minDate)) {
                                // Нашли новую минимальную дату - создаем новый список
                                List<Course> newList = new ArrayList<>();
                                newList.add(entry.getKey());
                                return new AbstractMap.SimpleEntry<>(newList, currentDate);
                            } else if (currentDate.equals(minDate)) {
                                // Та же минимальная дата - добавляем курс в список
                                acc.getKey().add(entry.getKey());
                                return acc;
                            }
                            // Дата больше минимальной - оставляем аккумулятор без изменений
                            return acc;
                        },

                        // Комбинатор для параллельных потоков
                        (acc1, acc2) -> {
                            if (acc1.getValue().isBefore(acc2.getValue())) {
                                return acc1;
                            } else if (acc2.getValue().isBefore(acc1.getValue())) {
                                return acc2;
                            } else {
                                // Если даты равны - объединяем списки
                                acc1.getKey().addAll(acc2.getKey());
                                return acc1;
                            }
                        }
                )
                .getKey();
    }

    public static List<Course> findAllCoursesWithLatestDate(List<Course> courses, DateTimeFormatter dateFormat) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        return courses.stream()
                .map(course -> {
                    // Парсим дату из строки курса
                    String dateStr = course.getStartDate().split("·")[0].trim();
                    LocalDate date = LocalDate.parse(dateStr, dateFormat);
                    // Создаем пару курс-дата
                    return new AbstractMap.SimpleEntry<>(course, date);
                })
                .reduce(
                        // Начальное значение аккумулятора - пустой список и минимально возможная дата
                        new AbstractMap.SimpleEntry<List<Course>, LocalDate>(new ArrayList<>(), LocalDate.MIN),

                        // Функция аккумуляции
                        (acc, entry) -> {
                            LocalDate currentDate = entry.getValue();
                            LocalDate maxDate = acc.getValue();

                            if (currentDate.isAfter(maxDate)) {
                                // Нашли новую максимальную дату - создаем новый список
                                List<Course> newList = new ArrayList<>();
                                newList.add(entry.getKey());
                                return new AbstractMap.SimpleEntry<>(newList, currentDate);
                            } else if (currentDate.equals(maxDate)) {
                                // Та же максимальная дата - добавляем курс в список
                                acc.getKey().add(entry.getKey());
                                return acc;
                            }
                            // Дата меньше максимальной - оставляем аккумулятор без изменений
                            return acc;
                        },

                        // Комбинатор для параллельных потоков
                        (acc1, acc2) -> {
                            if (acc1.getValue().isAfter(acc2.getValue())) {
                                return acc1;
                            } else if (acc2.getValue().isAfter(acc1.getValue())) {
                                return acc2;
                            } else {
                                // Если даты равны - объединяем списки
                                acc1.getKey().addAll(acc2.getKey());
                                return acc1;
                            }
                        }
                )
                .getKey();
    }

    public static List<Course> findCoursesOnOrAfterDate(List<Course> courses, String targetDateStr, DateTimeFormatter formatter) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate targetDate = LocalDate.parse(targetDateStr, formatter);

        return courses.stream()
                .filter(course -> {
                    try {
                        String dateStr = course.getStartDate().split("·")[0].trim();
                        LocalDate courseStartDate = LocalDate.parse(dateStr, formatter);
                        return !courseStartDate.isBefore(targetDate);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
