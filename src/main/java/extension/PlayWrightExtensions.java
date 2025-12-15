package extension;

import com.google.inject.Injector;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import di.DependencyInitializer;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayWrightExtensions implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver{
    private Injector injector;
    private Page page;
    private static Page staticPage;

    // Константы для трассировки
    private static final String TRACES_DIR = "playwright-traces";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final ThreadLocal<String> testName = new ThreadLocal<>();
    private static final ThreadLocal<String> className = new ThreadLocal<>();

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        // Останавливаем трассировку после каждого теста
        if (staticPage != null && staticPage.context() != null) {
            try {
                String timestamp = DATE_FORMAT.format(new Date());
                String traceFileName = String.format("%s_%s_%s.zip",
                        className.get(),
                        testName.get(),
                        timestamp);

                Path tracePath = Paths.get(TRACES_DIR, traceFileName);
                Files.createDirectories(tracePath.getParent());

                staticPage.context().tracing().stop(new Tracing.StopOptions()
                        .setPath(tracePath));

                System.out.println("Trace saved to: " + tracePath.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Failed to save trace: " + e.getMessage());
            }
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        // Сохраняем информацию о тесте для названия файла трассировки
        testName.set(extensionContext.getDisplayName()
                .replaceAll("[^a-zA-Z0-9.-]", "_")
                .replaceAll("_+", "_"));
        className.set(extensionContext.getTestClass()
                .map(Class::getSimpleName)
                .orElse("UnknownClass")
                .replaceAll("[^a-zA-Z0-9.-]", "_")
                .replaceAll("_+", "_"));

        // Очищаем куки и localStorage перед каждым тестом
        if (staticPage != null && staticPage.context() != null) {
            try {
                System.out.println("Очищаем куки");

                // 1. Очищаем куки
                staticPage.context().clearCookies();

                System.out.println("Куки очищены");

                // 4. Перезагружаем страницу чтобы очистить состояние приложения
                staticPage.reload();

                // 5. Ждем загрузки страницы
                staticPage.waitForLoadState();

            } catch (Exception e) {
                System.err.println("Ошибка при чистке cookie/storage" + e.getMessage());
            }
        }

        // Начинаем трассировку перед каждым тестом
        if (staticPage != null && staticPage.context() != null) {
            try {
                staticPage.context().tracing().start(new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true));
            } catch (Exception e) {
                System.err.println("Failed to start tracing: " + e.getMessage());
            }
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // Создаем директорию для трассировок
        try {
            Files.createDirectories(Paths.get(TRACES_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create traces directory: " + e.getMessage());
        }

        // Инициализируем статические зависимости
        staticPage = DependencyInitializer.getInstance(Page.class);

        // Настраиваем трассировку на уровне контекста
        if (staticPage != null && staticPage.context() != null) {
            try {
                // Можно добавить начальную конфигурацию трассировки
                System.out.println("Playwright tracing is enabled");
                System.out.println("Traces will be saved to: " +
                        Paths.get(TRACES_DIR).toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Failed to configure tracing: " + e.getMessage());
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Закрываем браузер после всех тестов в классе
        if (staticPage != null && staticPage.context() != null && staticPage.context().browser() != null) {
            try {
                // Сохраняем общую трассировку для всего тестового класса (опционально)
                String timestamp = DATE_FORMAT.format(new Date());
                String className = context.getTestClass()
                        .map(Class::getSimpleName)
                        .orElse("UnknownClass")
                        .replaceAll("[^a-zA-Z0-9.-]", "_")
                        .replaceAll("_+", "_");

                Path tracePath = Paths.get(TRACES_DIR,
                        String.format("%s_full_%s.zip", className, timestamp));

                // Останавливаем любую активную трассировку
                if (staticPage.context().tracing() != null) {
                    staticPage.context().tracing().stop(new Tracing.StopOptions()
                            .setPath(tracePath));
                }

                staticPage.context().browser().close();
                System.out.println("Browser closed. Full class trace saved to: " + tracePath.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Error during cleanup: " + e.getMessage());
                if (staticPage.context().browser() != null) {
                    staticPage.context().browser().close();
                }
            }
        }

        testName.remove();
        className.remove();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> paramType = parameterContext.getParameter().getType();
        return paramType.isAnnotationPresent(com.google.inject.Inject.class) ||
                paramType == Page.class ||
                paramType == config.TestConfig.class ||
                paramType == service.TeacherCarouselService.class ||
                paramType == service.TimeDurationCoursesService.class ||
                paramType == service.SubscriptionService.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> paramType = parameterContext.getParameter().getType();

        // Для Page возвращаем статический экземпляр
        if (paramType == Page.class) {
            return staticPage;
        }

        return DependencyInitializer.getInstance(paramType);
    }
}
