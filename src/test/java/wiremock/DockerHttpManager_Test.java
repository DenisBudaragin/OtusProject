package wiremock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class DockerHttpManager_Test {
    private final String dockerApiBaseUrl = "http://147.45.100.156:2375";
    private final String containerName = "wiremock";
    private final String wiremockBaseUrl = "http://147.45.100.156:8080";

    /**
     * Проверка доступности WireMock API
     */
    private boolean isWireMockAvailable() {
        try {
            URL url = new URL(wiremockBaseUrl + "/__admin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Ожидание доступности WireMock
     */
    private void waitForWireMock(int maxWaitSeconds) throws InterruptedException {
        for (int i = 0; i < maxWaitSeconds; i++) {
            if (isWireMockAvailable()) {
                System.out.println("WireMock доступен после " + i + " секунд ожидания");
                return;
            }
            Thread.sleep(1000);
        }
        throw new RuntimeException("WireMock не стал доступен за " + maxWaitSeconds + " секунд");
    }

    /**
     * Запуск контейнера через Docker HTTP API
     */
    public boolean startContainer() {
        return sendDockerRequest("/containers/" + containerName + "/start", "POST");
    }

    /**
     * Остановка контейнера через Docker HTTP API
     */
    public boolean stopContainer() {
        return sendDockerRequest("/containers/" + containerName + "/stop", "POST");
    }

    /**
     * Рестарт контейнера через Docker HTTP API
     */
    public boolean restartContainer() {
        return sendDockerRequest("/containers/" + containerName + "/restart", "POST");
    }

    /**
     * Получение статуса контейнера
     */
    public String getContainerStatus() {
        try {
            URL url = new URL(dockerApiBaseUrl + "/v1.41/containers/" + containerName + "/json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // В реальном приложении здесь нужно парсить JSON ответ
                // Для простоты возвращаем статус код как строку
                return "Доступен (код " + responseCode + ")";
            } else {
                return "Ошибка (код " + responseCode + ")";
            }
        } catch (IOException e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    private boolean sendDockerRequest(String endpoint, String method) {
        try {
            URL url = new URL(dockerApiBaseUrl + "/v1.41" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            System.out.println("Docker API ответил с кодом: " + responseCode);
            return responseCode >= 200 && responseCode < 300;

        } catch (IOException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            return false;
        }
    }

    @BeforeEach
    public void setUp() {
        System.out.println("Проверяем доступность Docker API и WireMock...");
        System.out.println("Статус контейнера: " + getContainerStatus());

        // Убедимся, что WireMock запущен перед тестами
        if (!isWireMockAvailable()) {
            System.out.println("WireMock не доступен, пытаемся запустить...");
            assertTrue(startContainer(), "Не удалось запустить контейнер");
            try {
                waitForWireMock(30); // Ждем до 30 секунд
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Ожидание прервано");
            }
        }
    }

    @Test
    @Order(1)
    public void testRestartContainer() {
        System.out.println("=== Тест рестарта контейнера ===");

        // Проверяем, что WireMock доступен перед рестартом
        assertTrue(isWireMockAvailable(), "WireMock должен быть доступен перед рестартом");

        // Выполняем рестарт
        assertTrue(restartContainer(), "Рестарт контейнера не удался");

        // Ждем, пока контейнер перезапустится
        try {
            Thread.sleep(3000); // Даем время на остановку
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Ожидание прервано");
        }

        // Ждем, пока WireMock снова станет доступен
        try {
            waitForWireMock(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Ожидание прервано");
        }

        // Проверяем, что WireMock снова доступен
        assertTrue(isWireMockAvailable(), "WireMock должен быть доступен после рестарта");

        System.out.println("Контейнер успешно перезапущен!");
    }
}