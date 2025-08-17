package asserts;

import helpers.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CourseFinderAsserts extends BaseTest {
    public void assertCourseNameMatches(WebDriver driver, String expectedCourseName) {
        String actualCourseName = driver.findElement(By.xpath("\n" +
                "//h1[text()=" + "'" + expectedCourseName + "']")).getText();

        Assert.assertEquals(actualCourseName, expectedCourseName,
                String.format("Открыта страница неверного курса. Ожидалось: '%s', но получено: '%s'",
                        expectedCourseName, actualCourseName));
    }
}
