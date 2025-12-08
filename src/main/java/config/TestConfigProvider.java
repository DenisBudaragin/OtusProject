package config;
import com.google.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfigProvider implements Provider<TestConfig>{
    @Override
    public TestConfig get() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("test-config.properties")) {

            if (input == null) {
                throw new RuntimeException("Не найден файл конфигурации test-config.properties");
            }

            properties.load(input);

            return new TestConfig(
                    properties.getProperty("base.url", "https://otus.ru"),
                    Boolean.parseBoolean(properties.getProperty("headless", "true")),
                    Integer.parseInt(properties.getProperty("timeout.ms", "10000")),
                    Integer.parseInt(properties.getProperty("slow.mo.ms", "100"))
            );

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации", e);
        }
    }
}
