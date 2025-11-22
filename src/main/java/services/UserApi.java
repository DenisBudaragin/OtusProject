package services;

import configs.Config;
import io.restassured.http.ContentType;
import pojo.User;
import specifications.RequestSpec;
import specifications.ResponseSpec;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserApi {
    public static void createUserSuccess(User user) {
        given()
                .spec(RequestSpec.baseRequestSpec())
                .body(user)
                .when()
                .post("/user")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec())
                .log().all();
    }

    public static void createUserError(String user) {
        given()
                .spec(RequestSpec.baseRequestSpec())
                .body(user)
                .when()
                .post("/user")
                .then()
                .spec(ResponseSpec.getErrorResponseSpec())
                .log().all();
    }

    public static void getUser(String userName){
        given()
                .spec(RequestSpec.baseRequestSpec())
                .pathParam("username", userName)
                .when()
                .get("/user/{username}")
                .then()
                .body(matchesJsonSchema(new File("src/main/java/schemas/user_response.json"))) // Валидация JSON схемы
                .log().all()
                .extract()
                .response();
    }

    public static void deleteUser(String userName){
        given()
                .spec(RequestSpec.baseRequestSpec())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParam("username", userName)
                .when()
                .delete("/user/{username}")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec())
                .body("message", equalTo(userName))
                .log().all()
                .extract()
                .response();
    }

    public static void deleteUserError(String userName){
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
