package configa;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Config {
    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

    public static final String COURSE_NAME = "Reinforcement Learning";
    public static final String COURSE_XPATH = "//h6/div[contains(text(), 'Reinforcement Learning')]";
    public static final String OTUS_MAIN_PAGE = "https://otus.ru";
    public static final String OTUS_COURSES_PAGE = "https://otus.ru/catalog/courses";
}
