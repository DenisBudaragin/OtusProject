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

        //Создаем пользователя
        UserApi.createUserSuccess(user);

        //Удаляем пользователя
        UserApi.deleteUser(userName);
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
