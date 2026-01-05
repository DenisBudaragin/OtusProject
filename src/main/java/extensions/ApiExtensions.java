package extensions;

import com.google.inject.Guice;
import modules.ServiceModule;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApiExtensions implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        Guice.createInjector(new ServiceModule()).injectMembers(
                context.getTestInstance()
                        .orElseThrow(() -> new RuntimeException("Error"))

        );
    }
}
