package services;

import helpers.UserHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import models.UserModel;
import com.google.inject.Inject;

public class UserService extends AbsBaseService {
    @Inject
    private UserHelper userhelper;

    public UserModel getUserById(int id) {
        Response response =  RestAssured.given(rewuestSpecification())
                .when()
                .get("/user/get/" + id);

                return userhelper
                        .extractResultFromArray(response, UserModel.class)
                        .stream().filter((UserModel userModel) -> userModel.getId() == id).findFirst()
                        .orElse(null); // или orElseThrow с кастомным исключением
    }

    public Response getAllUsers() {
        return RestAssured.given()
                .when()
                .get("/user/get/all");
    }
}
