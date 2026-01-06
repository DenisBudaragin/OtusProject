package helpers;

import models.UserModel;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

import java.util.List;

public class UserHelper {
    public List<UserModel> extractResultFromArray(Response response, Class<UserModel> clazz) {
        JsonPath jsonPath = response.then().extract().jsonPath();

        return jsonPath.getList("", clazz);

    }
}
