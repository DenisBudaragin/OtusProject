package driver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.appium.java_client.android.AndroidDriver;

public class DriverModule extends AbstractModule {

    @Provides
    @Singleton
    public AndroidDriver provideAndroidDriver() {
        return DriverManager.getInstance().getDriver();
    }

    @Override
    protected void configure() {
        // Дополнительные настройки Guice при необходимости
    }
}