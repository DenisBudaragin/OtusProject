package fixtures;

import org.junit.jupiter.api.extension.*;
import services.UserApi;
import java.util.HashSet;
import java.util.Set;

public class UserCleanupExtension implements
        AfterEachCallback, AfterAllCallback {

    private static final Set<String> createdUsers = new HashSet<>();

    /**
     * Регистрирует созданного пользователя для последующего удаления
     */
    public static void registerUserForCleanup(String username) {
        if (username != null && !username.isEmpty()) {
            synchronized (createdUsers) {
                createdUsers.add(username);
            }
        }
    }

    private void cleanupAllUsers() {
        Set<String> usersToCleanup;
        synchronized (createdUsers) {
            usersToCleanup = new HashSet<>(createdUsers);
            createdUsers.clear();
        }

        for (String username : usersToCleanup) {
            try {
                UserApi.deleteUser(username);
                System.out.println("Cleaned up user: " + username);
            } catch (Exception e) {
                System.out.println("Failed to delete user " + username + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (context.getTestClass().isPresent()) {
            Class<?> testClass = context.getTestClass().get();
            if (testClass.isAnnotationPresent(CleanupUsers.class)) {
                CleanupUsers annotation = testClass.getAnnotation(CleanupUsers.class);
                if (annotation.enabled()) {
                    cleanupAllUsers();
                }
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        cleanupAllUsers();
    }
}
