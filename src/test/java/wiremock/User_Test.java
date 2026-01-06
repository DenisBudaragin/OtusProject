package wiremock;

import com.google.inject.Inject;
import models.UserModel;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.util.Random;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class User_Test {

    @Inject
    private UserService userService;

    @Test
    public void checkGetUser() {
        UserModel userModel = userService.getUserById(new Random().nextInt());

        assertSoftly(softly -> {
            softly.assertThat(userModel.getName())
                    .isEqualTo("Test user");
        });
    }
}
