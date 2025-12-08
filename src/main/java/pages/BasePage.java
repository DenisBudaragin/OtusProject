package pages;
import com.microsoft.playwright.Page;
import config.TestConfig;

public abstract  class BasePage {
    protected final Page page;
    protected final TestConfig config;

    public BasePage(Page page, TestConfig config) {
        this.page = page;
        this.config = config;
    }

    public void navigateTo(String url) {
        page.navigate(url);
    }
}
