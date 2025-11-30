package helpers;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    private BaseTest baseTest;

    public Hooks() {
        this.baseTest = BaseTest.getInstance();
    }

    @Before
    public void setup() {
        if (baseTest.getDriver() == null) {
            baseTest.setup();
        }
    }

    @After
    public void teardown() {
        baseTest.teardown();
    }
}