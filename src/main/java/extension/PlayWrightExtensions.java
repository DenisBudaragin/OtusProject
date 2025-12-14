package extension;

import com.google.inject.Injector;
import com.microsoft.playwright.Page;
import di.DependencyInitializer;
import org.junit.jupiter.api.extension.*;

public class PlayWrightExtensions implements BeforeAllCallback, AfterAllCallback, ParameterResolver{
    private Injector injector;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // Можно инициализировать общие ресурсы здесь если нужно
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        // Очистка ресурсов после всех тестов
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
        return DependencyInitializer.getInstance(paramType);
    }
}
