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


import java.time.Duration;
import org.junit.jupiter.api.Test;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;

//@WireMockTest(httpPort = 8080)
public class Service_Test {

//    private static WireMockServer wireMockServer = null;
//    private static final String WIREMOCK_HOST = "147.45.100.156"; // IP вашего Docker хоста
//    private static final int WIREMOCK_PORT = 8080;
//
//    @BeforeAll
//    public static void init() throws JsonProcessingException {
//        // Если WireMock уже запущен в Docker, не создаем локальный сервер
//        // Просто конфигурируем клиент для удаленного WireMock
//        configureFor(WIREMOCK_HOST, WIREMOCK_PORT);
//
//        // Создаем экземпляр модели
//        UserModel user = new UserModel();
//        // Можно переопределить значения по умолчанию
//        user.setName("Иван Иванов");
//        user.setCourse("Java QA Engineer");
//        user.setEmail("ivan@test.ru");
//        user.setAge(25);
//        user.setId(123);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonResponse = mapper.writeValueAsString(user);
//
//        // Настраиваем стаб для endpoint /user/get/all
//        stubFor(get(urlEqualTo("/user/get/all"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json; charset=UTF-8")
//                        .withBody(jsonResponse)));
//    }
//
//    @AfterAll
//    public static void close() {
//        if (wireMockServer != null) {
//            wireMockServer.stop();
//        }
//    }
//
//    @Test
//    public void checkWireMock() throws IOException, InterruptedException {
//        // Создаем HTTP клиент
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("http://localhost:8080/test/user"))
//                .GET()
//                .build();
//
//        // Отправляем запрос
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        // Проверяем результат
//        System.out.println("Status Code: " + response.statusCode());
//        System.out.println("Response Body: " + response.body());
//
//        // Можно добавить assert для проверки
//        // assertEquals(200, response.statusCode());
//        // assertEquals("Привет!", response.body());
//    }


    private static final String WIREMOCK_HOST = "147.45.100.156";
    private static final int WIREMOCK_PORT = 8080;
    private static final String BASE_URL = "http://" + WIREMOCK_HOST + ":" + WIREMOCK_PORT;

    @Test
    public void testGetAllUsers() throws IOException, InterruptedException {
        // Создаем HTTP клиент с таймаутами
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // Отправляем GET запрос к endpoint
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/user/get/all"))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .build();

        // Получаем ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Выводим результат
        System.out.println("=== Response Details ===");
        System.out.println("URL: " + request.uri());
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Headers: " + response.headers().map());
        System.out.println("Response Body: " + response.body());
        System.out.println("======================");
    }
}
