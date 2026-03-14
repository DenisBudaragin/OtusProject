package asserts;

import helpers.AllureHelper;
import org.junit.jupiter.api.Assertions;

public class WishItemAsserts {

    public static void assertStatusChanged(String oldStatus, String newStatus) {
        AllureHelper.attachText("Status change check",
                "Old: " + oldStatus + ", New: " + newStatus);
        Assertions.assertNotEquals(oldStatus, newStatus,
                "Status should change after toggling");
    }

    public static void assertStatusNotChanged(String oldStatus, String newStatus) {
        AllureHelper.attachText("Status unchanged check",
                "Old: " + oldStatus + ", New: " + newStatus);
        Assertions.assertEquals(oldStatus, newStatus,
                "Status should not change");
    }

    public static void assertStatusIs(String actualStatus, String expectedStatus, String itemName) {
        String actualStatusText = actualStatus.equals("true") ? "RESERVED" : "NOT RESERVED";
        String expectedStatusText = expectedStatus.equals("true") ? "RESERVED" : "NOT RESERVED";

        AllureHelper.attachText("Status check for " + itemName,
                "Expected: " + expectedStatusText + ", Actual: " + actualStatusText);
        Assertions.assertEquals(expectedStatus, actualStatus,
                "Item '" + itemName + "' should have status: " + expectedStatusText);
    }
}