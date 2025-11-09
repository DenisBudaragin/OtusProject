package specifications;
import io.restassured.specification.ResponseSpecification;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;

public class ResponseSpec {
    public static ResponseSpecification getSuccessResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .expectBody("code", equalTo(200))
                .expectBody("type", equalTo("unknown"))
                .expectBody("message", notNullValue())
                .build();
    }
    public static ResponseSpecification getErrorResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(500)
                .expectContentType(ContentType.JSON)
                .expectBody("code", equalTo(500))
                .expectBody("type", equalTo("unknown"))
                .expectBody("message", equalTo("something bad happened"))
                .build();
    }
}
