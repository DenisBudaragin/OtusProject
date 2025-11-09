package main;

import configs.Config;
import helpers.RandomGenerator;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import pojo.User;
import specifications.RequestSpec;
import specifications.ResponseSpec;
import java.io.File;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateAndGetTest {
    String name = Config.userName;

     /*
     * Автотест проверяет создание пользователя (позитивный сценарий)
     * отправляется запрос POST /v2/user
     * и отправляется запрос на получение созданного пользователя GET /v2/{user_name}
     * затем валидируется полученное толо по json-схеме
     * валидируется статус код, type, message в теле ответа
     */

    @Test
    public void createAndGetUser() {

        User user = new User();
        user.setId(0);
        user.setUsername(Config.userName);
        user.setFirstName("TestUser_" + RandomGenerator.generateRandomString(7));
        user.setLastName("TestUser_" + RandomGenerator.generateRandomString(7));
        user.setEmail(RandomGenerator.generateRandomString(7) + "@gmail.com");
        user.setPassword("123");
        user.setPhone("321");
        user.setUserStatus(1);

        given()
                .spec(RequestSpec.baseRequestSpec())
                .body(user)
        .when()
                .post("/user")
        .then()
                .spec(ResponseSpec.getSuccessResponseSpec())
                .log().all();

        //Получаем информацию по созданному пользователю
        given()
                .spec(RequestSpec.baseRequestSpec())
                .pathParam("username", Config.userName) // устанавливаем значение параметра
                .when()
                .get("/user/{username}") // используем плейсхолдер
                .then()
                .body(matchesJsonSchema(new File("src/main/java/schemas/user_response.json"))) // Валидация JSON схемы
                .log().all()
                .extract()
                .response();
    }

    /* Негативный сценарий
     * Автотест проверяет попытку создания пользователя с некорректными типами данных
     * отправляется запрос POST /v2/user
     * валидируется статус код, type, message в теле ответа
     */

    @Test
    public void createUserError() {
        String invalidJson = """
        {
            "id": "not_a_number",
            "username": "testuser",
            "firstName": "John",
            "lastName": "Doe",
            "email": "test@gmail.com",
            "password": "123",
            "phone": "1234567890",
            "userStatus": "active"
        }
        """;

        given()
                .spec(RequestSpec.baseRequestSpec())
                .body(invalidJson)
                .when()
                .post("/user")
                .then()
                .spec(ResponseSpec.getErrorResponseSpec())
                .log().all();
    }

    /*
     * Автотест проверяет удаление пользователя
     * отправляется запрос DELETE /v2/{user_name}
     * валидируется статус код, type, message в теле ответа
     */

    @Test
    public void deleteUserByName() {

        given()
                .spec(RequestSpec.baseRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("username", Config.userName)
                .when()
                .delete("/user/{username}")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec())
                .body("message", equalTo(Config.userName))
                .log().all()
                .extract()
                .response();
    }

    /* Негативный сценарий
     * Автотест проверяет удаление пользователя с пустым именем
     * отправляется запрос DELETE /v2/{user_name}
     * валидируется статус код
     */

    @Test
    public void deleteUserWithEmptyUsername() {
        given()
                .spec(RequestSpec.baseRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("username", "")
                .when()
                .delete("/user/{username}")
                .then()
                .statusCode(405)
                .log().all();
    }
}
