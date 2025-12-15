package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonthPhraseHelper {
    public static String extractMonthPhrase(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        // Регулярное выражение для поиска числа и слова "месяц" в любом падеже
        Pattern pattern = Pattern.compile("(\\d+\\s*месяц(?:а|ов|ев)?)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        return null;
    }

    public static boolean validateMonth(String input) {
        // Извлекаем все цифры из строки
        String numberStr = input.replaceAll("[^0-9]", "").trim();

        if (numberStr.isEmpty()) {
            return false;
        }

        try {
            int months = Integer.parseInt(numberStr);
            return months >= 3 && months <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
