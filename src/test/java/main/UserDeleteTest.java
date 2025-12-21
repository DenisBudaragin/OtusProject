package main;

import helpers.RandomGenerator;
import org.junit.jupiter.api.Test;
import pojo.User;
import services.UserApi;
import static org.junit.jupiter.api.Assertions.*;

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
        // Получаем пользователя и проверяем его данные перед удалением
        User responseUser = UserApi.getUser(userName);

        assertEquals(user.getId(), responseUser.getId(), "ID пользователя не совпадает");
        assertEquals(user.getUsername(), responseUser.getUsername(), "Username не совпадает");
        assertEquals(user.getFirstName(), responseUser.getFirstName(), "FirstName не совпадает");
        assertEquals(user.getLastName(), responseUser.getLastName(), "LastName не совпадает");
        assertEquals(user.getEmail(), responseUser.getEmail(), "Email не совпадает");
        assertEquals(user.getPhone(), responseUser.getPhone(), "Phone не совпадает");
        assertEquals(user.getUserStatus(), responseUser.getUserStatus(), "UserStatus не совпадает");

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
