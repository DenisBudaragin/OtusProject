package main;

import fixtures.CleanupUsers;
import fixtures.UserCleanupExtension;
import fixtures.UserFixtures;
import org.junit.jupiter.api.extension.ExtendWith;
import helpers.RandomGenerator;
import org.junit.jupiter.api.Test;
import pojo.User;
import services.UserApi;
import com.github.javafaker.Faker;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(UserCleanupExtension.class)
@CleanupUsers(enabled = true)
public class UserCreateAndGetTest {
    private static final Faker faker = new Faker();
    /*
     * Автотест проверяет создание пользователя (позитивный сценарий)
     * отправляется запрос POST /v2/user
     * и отправляется запрос на получение созданного пользователя GET /v2/{user_name}
     * затем валидируется полученное толо по json-схеме
     * валидируется статус код, type, message в теле ответа
     */

    @Test
    public void createAndGetUser() {
        String userName = RandomGenerator.generateFirstName();
        User user = new User();
        user.setId(RandomGenerator.generateUserId());
        user.setUsername(userName);
        user.setFirstName(RandomGenerator.generateFirstName());
        user.setLastName(RandomGenerator.generateLastName());
        user.setEmail(RandomGenerator.generateEmail());
        user.setPassword(RandomGenerator.generatePassword(8, 12));
        user.setPhone(RandomGenerator.generatePhoneNumber());
        user.setUserStatus(1);

        UserApi.createUserSuccess(user);

        // Регистрируем пользователя для автоматического удаления
        UserFixtures.registerUserForCleanup(userName);

        User responseUser = UserApi.getUser(userName);

        assertEquals(user.getId(), responseUser.getId(), "ID пользователя не совпадает");
        assertEquals(user.getUsername(), responseUser.getUsername(), "Username не совпадает");
        assertEquals(user.getFirstName(), responseUser.getFirstName(), "FirstName не совпадает");
        assertEquals(user.getLastName(), responseUser.getLastName(), "LastName не совпадает");
        assertEquals(user.getEmail(), responseUser.getEmail(), "Email не совпадает");
        assertEquals(user.getPhone(), responseUser.getPhone(), "Phone не совпадает");
        assertEquals(user.getUserStatus(), responseUser.getUserStatus(), "UserStatus не совпадает");
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
