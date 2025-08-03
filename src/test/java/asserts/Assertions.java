package asserts;

import helpers.RandomCategorySelectorHelper;
import utils.Course;

import java.util.List;
import java.util.regex.Pattern;
import static org.testng.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;


public class Assertions
{
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
    public static void assertUrlsAfterDelimitersEqual(String urlWithEquals, String urlWithSlash) {
        String processed1 = RandomCategorySelectorHelper.extractAfterEquals(urlWithEquals);
        String processed2 = RandomCategorySelectorHelper.extractAfterLastSlash(urlWithSlash);

        org.junit.jupiter.api.Assertions.assertEquals(processed1, processed2,
                "Части URL после разделителей не совпадают");
    }

}
