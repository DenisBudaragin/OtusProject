package configs;

import helpers.RandomGenerator;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Config {
    public static String baseUri = "https://petstore.swagger.io/v2";

    public static String getBaseUri() {
        return baseUri;
    }

    public static void setBaseUri(String uri) {
        baseUri = uri;
    }
}
