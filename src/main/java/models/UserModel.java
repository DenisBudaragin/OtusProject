package models;

import lombok.Data;

@Data
public class UserModel {
    private String name = "Test user";
    private String course = "QA";
    private String email = "test@test.test";
    private int age = 23;
    private int id = 1;
}
