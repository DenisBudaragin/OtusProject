package helpers;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Config {
//    public static final String CATALOG_URL = "https://otus.ru/catalog/courses";

    public static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("d MMMM, yyyy", new Locale("ru"));

    public static final String COURSE_NAME = "3D Artist"; // Замените на имя курса для поиска
    public static final String COURSE_XPATH = "//h6/div[contains(text(), '3D Artist')]"; // Замените на имя курса для поиска
    public static final String OTUS_MAIN_PAGE = "https://otus.ru";
    public static final String CATEGORY_NAME = "Обучение";
}
