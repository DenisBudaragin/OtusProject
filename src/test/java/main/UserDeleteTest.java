package main;

import configs.Config;
import helpers.RandomGenerator;
import org.junit.jupiter.api.Test;
import pojo.User;
import services.UserApi;

public class UserDeleteTest {
    /*
     * Автотест проверяет удаление пользователя
     * отправляется запрос DELETE /v2/{user_name}
     * валидируется статус код, type, message в теле ответа
     */

    @Test
    public void deleteUserByName() {
        User user = new User();
        user.setId(0);
        user.setUsername(Config.userName);
        user.setFirstName("TestUser_" + RandomGenerator.generateRandomString(7));
        user.setLastName("TestUser_" + RandomGenerator.generateRandomString(7));
        user.setEmail(RandomGenerator.generateRandomString(7) + "@gmail.com");
        user.setPassword("123");
        user.setPhone("321");
        user.setUserStatus(1);

        //Создаем пользователя
        UserApi.createUserSuccess(user);

        //Удаляем пользователя
        UserApi.deleteUser(Config.userName);
    }

    /* Негативный сценарий
     * Автотест проверяет удаление пользователя с пустым именем
     * отправляется запрос DELETE /v2/{user_name}
     * валидируется статус код
     */

    @Test
    public void deleteUserWithEmptyUsername() {
        UserApi.deleteUserError("");
    }
}
