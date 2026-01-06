package services;
import com.google.inject.Guice;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import modules.ServiceModule;

public abstract class AbsBaseService {

    public AbsBaseService() {
        Guice.createInjector(new ServiceModule()).injectMembers(this);
    }

    protected RequestSpecification rewuestSpecification() {
        RestAssured.defaultParser = Parser.JSON;
        return RestAssured.given()
                .baseUri(System.getProperty("base.url"))
                .basePath("/wiremock");

    }

    protected ResponseSpecification responseSpecification() {
        RestAssured.defaultParser = Parser.JSON;
        return RestAssured.given()
                .then()
                .statusCode(200);
    }
}
