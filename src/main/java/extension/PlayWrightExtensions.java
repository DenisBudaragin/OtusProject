package extension;

import com.google.inject.Injector;
import com.microsoft.playwright.Page;
import di.DependencyInitializer;
import org.junit.jupiter.api.extension.*;

public class PlayWrightExtensions implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver{
    private Injector injector;
    private Page page;
    private static Page staticPage;

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // Инициализируем статические зависимости
        staticPage = DependencyInitializer.getInstance(Page.class);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Закрываем браузер после всех тестов в классе
        if (staticPage != null && staticPage.context() != null && staticPage.context().browser() != null) {
            staticPage.context().browser().close();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> paramType = parameterContext.getParameter().getType();
        return paramType.isAnnotationPresent(com.google.inject.Inject.class) ||
                paramType == Page.class ||
                paramType == config.TestConfig.class ||
                paramType == service.TeacherCarouselService.class;
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
