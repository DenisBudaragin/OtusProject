package di;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class DependencyInitializer {
    private static final Injector injector = Guice.createInjector(new DependencyContainer());

    public static <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }
}
