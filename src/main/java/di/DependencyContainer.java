package di;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import config.TestConfig;
import config.TestConfigProvider;

import java.util.Arrays;

public class DependencyContainer extends AbstractModule{
    @Override
    protected void configure() {
        bind(TestConfig.class).toProvider(TestConfigProvider.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Playwright providePlaywright() {
        return Playwright.create();
    }

    @Provides
    @Singleton
    public Browser provideBrowser(Playwright playwright, TestConfig config) {
        return playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setSlowMo(500) // Пауза 500мс между действиями
//                        .setDevtools(true) // Открыть DevTools
                        .setArgs(Arrays.asList(
                                "--start-fullscreen",  // Открыть сразу в полноэкранном режиме
                                "--disable-notifications"
                        ))
                        .setHeadless(config.isHeadless())
                        .setSlowMo(config.getSlowMo())
        );
    }

    @Provides
    @Singleton
    public BrowserContext provideBrowserContext(Browser browser, TestConfig config) {
        BrowserContext context = browser.newContext();
        context.setDefaultTimeout(config.getTimeout());
        return context;
    }

    @Provides
    @Singleton
    public Page providePage(BrowserContext context) {
        return context.newPage();
    }
}
