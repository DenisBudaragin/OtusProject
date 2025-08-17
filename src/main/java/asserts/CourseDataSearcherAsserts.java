package asserts;
import utils.Course;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CourseDataSearcherAsserts {
    public static void assertNoDateOnlyCourses(List<Course> courses) {
        Pattern pattern = Pattern.compile("^(.+?)\\s+\\d{1,2}\\s+[а-я]+,\\s+\\d{4}(\\s+·.*)?$");
        for (Course course : courses) {
            String courseInfo = course.getTitle();

            assertAll(
                    () -> Assertions.assertTrue(pattern.matcher(courseInfo).matches(),
                            () -> String.format("Неверный формат курса: %s", courseInfo)),
                    () -> {
                        String namePart = courseInfo.split("\\d{1,2}\\s+[а-я]+,\\s+\\d{4}")[0].trim();
                        Assertions.assertTrue(namePart.length() >= 3,
                                "Название курса должно содержать минимум 3 символа: " + namePart);
                        Assertions.assertTrue(namePart.matches(".*[а-яА-Яa-zA-Z].*"),
                                "Название должно содержать буквы: " + namePart);
                    }
            );
        }
    }
}
