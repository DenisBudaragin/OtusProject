package configa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties не найден в classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки config.properties", e);
        }
    }

    /**
     * Получает свойство по ключу.
     * Приоритет: системное свойство > значение из файла > пустая строка
     */
    public static String getProperty(String key) {
        String sysProp = System.getProperty(key);
        return (sysProp != null && !sysProp.trim().isEmpty())
                ? sysProp
                : properties.getProperty(key, "");
    }

    /**
     * Получает свойство по ключу с указанием значения по умолчанию.
     * Приоритет: системное свойство > значение из файла > значение по умолчанию
     */
    public static String getProperty(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp;
        }

        String fileValue = properties.getProperty(key);
        return (fileValue != null && !fileValue.trim().isEmpty())
                ? fileValue
                : defaultValue;
    }

    /**
     * Получает булево значение свойства.
     * Приоритет: системное свойство > значение из файла > значение по умолчанию
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * Проверяет, запущен ли режим в удалённом режиме (remote)
     */
    public static boolean isRemoteMode() {
        return "remote".equalsIgnoreCase(getProperty("run.mode"));
    }
}
