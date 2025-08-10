package helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Course;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

public class CourseDataSearcherHelper {
    public <T extends Course> List<T> parseCourses(String url, Function<Element, T> courseCreator) throws IOException {
        List<T> courses = new ArrayList<>();
        Document doc = Jsoup.connect(url).get();
        Elements courseElements = doc.select("div.sc-18q05a6-0.incGfX > div > a");

        for (Element courseElement : courseElements) {
            String title = courseElement.text();
            String dateText = courseElement.lastElementChild().text();

            if (dateText != null) {
                T course = courseCreator.apply(courseElement);
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Course> findAllCoursesWithEarliestDate(List<Course> courses, DateTimeFormatter dateFormat) {
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

    public List<Course> findAllCoursesWithLatestDate(List<Course> courses, DateTimeFormatter dateFormat) {
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
}
