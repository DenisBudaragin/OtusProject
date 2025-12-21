package helpers;

import com.github.javafaker.Faker;
import java.util.Locale;

public class RandomGenerator {
    private static final ThreadLocal<Faker> fakerThreadLocal = ThreadLocal.withInitial(() ->
            new Faker(new Locale("en"))
    );
    public static String generateRandomString(int length) {
        return fakerThreadLocal.get().regexify("[a-zA-Z0-9]{" + length + "}");
    }

    public static String generateFirstName() {
        return fakerThreadLocal.get().name().firstName();
    }

    public static String generateLastName() {
        return fakerThreadLocal.get().name().lastName();
    }

    public static String generateEmail() {
        return fakerThreadLocal.get().internet().emailAddress();
    }

    public static String generateUsername() {
        return fakerThreadLocal.get().name().username();
    }

    public static String generatePhoneNumber() {
        return fakerThreadLocal.get().phoneNumber().cellPhone();
    }

    public static String generatePassword(int minLength, int maxLength) {
        return fakerThreadLocal.get().internet().password(minLength, maxLength, true, true, true);
    }

    public static int generateUserId() {
        return fakerThreadLocal.get().number().numberBetween(1, 10000);
    }
}
