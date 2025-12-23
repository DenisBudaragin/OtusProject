package fixtures;

public class UserFixtures {
    public static void registerUserForCleanup(String username) {
        UserCleanupExtension.registerUserForCleanup(username);
    }
}