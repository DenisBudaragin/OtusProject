package main;

import configs.Config;
import helpers.RandomGenerator;
import org.junit.jupiter.api.Test;
import pojo.User;
import services.UserApi;

public class UserCreateAndGetTest {
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

        UserApi.createUserSuccess(user);
        UserApi.getUser(Config.userName);
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

        UserApi.createUserError(invalidJson);
    }
}
