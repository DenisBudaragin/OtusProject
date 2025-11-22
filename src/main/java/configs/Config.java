package configs;

import helpers.RandomGenerator;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Config {
    public static final String BASE_URI = "https://petstore.swagger.io/v2";
    public static String userName = "Name_" + RandomGenerator.generateRandomString(7);
}
