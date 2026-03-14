package driver;

import com.google.inject.Singleton;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;

@Singleton
public class DriverManager {
    private static final String APPIUM_SERVER_URL = "http://109.73.196.103:4723";
    private static final String PLATFORM_VERSION = "13";
    private static final String DEVICE_NAME = "redroid13";
    private static final String APP_PACKAGE = "ru.otus.wishlist";
    private static final String APP_ACTIVITY = "ru.otus.wishlist.MainActivity";

    private AndroidDriver driver;
    private static DriverManager instance;

    private DriverManager() {
        initializeDriver();
    }

    public static synchronized DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    private void initializeDriver() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("appium:platformVersion", PLATFORM_VERSION);
            capabilities.setCapability("appium:deviceName", DEVICE_NAME);
            capabilities.setCapability("appium:automationName", "UiAutomator2");
            capabilities.setCapability("appium:appPackage", APP_PACKAGE);
            capabilities.setCapability("appium:appActivity", APP_ACTIVITY);
            capabilities.setCapability("appium:noReset", true);
            capabilities.setCapability("appium:fullReset", false);
            capabilities.setCapability("appium:appWaitForLaunch", true);
            capabilities.setCapability("appium:appWaitDuration", 60000);
            capabilities.setCapability("appium:autoGrantPermissions", true);
            capabilities.setCapability("appium:autoAcceptAlerts", true);
            capabilities.setCapability("appium:newCommandTimeout", 600);
            capabilities.setCapability("appium:adbExecTimeout", 60000);
            capabilities.setCapability("appium:uiautomator2ServerInstallTimeout", 120000);
            capabilities.setCapability("appium:androidInstallTimeout", 120000);

            driver = new AndroidDriver(new URL(APPIUM_SERVER_URL), capabilities);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize AndroidDriver", e);
        }
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            instance = null;
        }
    }
}