package helpers;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class RandomDataGenerator {
    private final Random random = new Random();

    private final String[] PHONE_MODELS = {
            "iPhone 15 Pro", "Samsung Galaxy S24", "Google Pixel 8",
            "Xiaomi 14", "OnePlus 12"
    };

    private final String[] NEW_PHONE_MODELS = {
            "Samsung Galaxy S24 Ultra", "iPhone 16", "Google Pixel 9",
            "Xiaomi 15 Pro", "Nothing Phone 3"
    };

    private final String[] DESCRIPTIONS = {
            "Флагманский смартфон", "Отличная камера", "Быстрый процессор",
            "Яркий экран", "Долгая работа"
    };

    private final String[] NEW_DESCRIPTIONS = {
            "Улучшенная версия", "Новинка 2025", "Топ-модель",
            "Премиум качество", "Инновационный дизайн"
    };

    public String randomPhoneModel() {
        return PHONE_MODELS[random.nextInt(PHONE_MODELS.length)] + " " + RandomStringUtils.randomNumeric(2);
    }

    public String randomNewPhoneModel() {
        return NEW_PHONE_MODELS[random.nextInt(NEW_PHONE_MODELS.length)] + " " + RandomStringUtils.randomNumeric(2);
    }

    public String randomPrice() {
        return String.valueOf(50 + random.nextInt(2000));
    }

    public String randomDescription() {
        return DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)] + " " + RandomStringUtils.randomAlphabetic(5);
    }

    public String randomNewDescription() {
        return NEW_DESCRIPTIONS[random.nextInt(NEW_DESCRIPTIONS.length)] + " " + RandomStringUtils.randomAlphabetic(6);
    }

    public String randomWishListTitle() {
        return "DenTest wish";
    }

    public String randomEditedWishListTitle() {
        return "DenisTest wish edited " + System.currentTimeMillis();
    }
}