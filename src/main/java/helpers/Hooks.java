package helpers;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public static void setup() {
        if (BaseTest.getDriver() == null) {
            BaseTest.setup();
        }
    }

    @After
    public static void teardown() {
        BaseTest.teardown();
    }
}