package wiremock;

import com.google.inject.Inject;
import models.UserModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specifications.RequestSpec;
import specifications.ResponseSpec;
//import services.UserService;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class User_Test {
    @Test
    @DisplayName("GET /user/get/all - Получение всех пользователей")
    public void testGetAllUsers() {
        given()
                .spec(RequestSpec.getRequestSpec())
                .when()
                .get("/user/get/all")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec());
    }

    @Test
    @DisplayName("GET /course/get/all - Получение всех курсов")
    public void testGetAllCourses() {
        given()
                .spec(RequestSpec.getRequestSpec())
                .when()
                .get("/course/get/all")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec());
    }

    @Test
    @DisplayName("GET /user/get/5 - Получение пользователя по ID 5")
    public void testGetUserById() {
        given()
                .spec(RequestSpec.getRequestSpec())
                .when()
                .get("/user/get/5")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec());
    }

    @Test
    @DisplayName("GET /get/estimate - Получение оценки")
    public void testGetEstimate() {
        given()
                .spec(RequestSpec.getRequestSpec())
                .when()
                .get("/get/estimate")
                .then()
                .spec(ResponseSpec.getSuccessResponseSpec());
    }

    @Test
    @DisplayName("POST /soap/user - Получаем пользователя в XML виде")
    public void testPostSoapUser() {
        given()
                .spec(RequestSpec.getXmlRequestSpec())
                .when()
                .post("/soap/user")
                .then()
                .spec(ResponseSpec.getXmlSuccessResponseSpec());
    }
}
