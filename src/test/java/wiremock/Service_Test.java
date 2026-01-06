package wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import models.UserModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

//@WireMockTest(httpPort = 8080)
public class Service_Test {

    private static WireMockServer wireMockServer = null;

    @BeforeAll
    public static void init() throws JsonProcessingException {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();

        configureFor("localhost", 8080);

        // Создаем экземпляр модели
        UserModel user = new UserModel();
        // Можно переопределить значения по умолчанию
        user.setName("Иван Иванов");
        user.setCourse("Java QA Engineer");
        user.setEmail("ivan@test.ru");
        user.setAge(25);
        user.setId(123);

        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(user);

        stubFor(get(urlEqualTo("/test/user"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json; charset=UTF-8")
                        .withBody(jsonResponse)));
    }

    @AfterAll
    public static void close() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void checkWireMock() throws IOException, InterruptedException {
        // Создаем HTTP клиент
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/test/user"))
                .GET()
                .build();

        // Отправляем запрос
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем результат
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        // Можно добавить assert для проверки
        // assertEquals(200, response.statusCode());
        // assertEquals("Привет!", response.body());
    }
}
