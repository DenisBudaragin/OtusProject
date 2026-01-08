package wiremock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.UserModel;
import org.junit.jupiter.api.Test;

public class WireMockApiClient_Test {
    private static final String WIREMOCK_HOST = "147.45.100.156";
    private static final int WIREMOCK_PORT = 8080;
    private static final String BASE_URL = "http://" + WIREMOCK_HOST + ":" + WIREMOCK_PORT;

    @Test
    public void configureStub() throws Exception {
        // Создаем тестовые данные
        UserModel user = new UserModel();
        user.setName("Иван Иванов");
        user.setCourse("Java QA Engineer");
        user.setEmail("ivan@test.ru");
        user.setAge(25);
        user.setId(123);

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody = mapper.writeValueAsString(user);

        // Формируем запрос для WireMock API
        String stubJson = String.format(
                "{\n" +
                        "  \"request\": {\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"url\": \"/user/get/all\"\n" +
                        "  },\n" +
                        "  \"response\": {\n" +
                        "    \"status\": 200,\n" +
                        "    \"headers\": {\n" +
                        "      \"Content-Type\": \"application/json\"\n" +
                        "    },\n" +
                        "    \"jsonBody\": %s\n" +
                        "  }\n" +
                        "}",
                jsonBody
        );

        // Отправляем запрос в WireMock
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/__admin/mappings/new"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(stubJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }
}
