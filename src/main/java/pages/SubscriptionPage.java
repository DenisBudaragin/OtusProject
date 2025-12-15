package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import config.TestConfig;

public class SubscriptionPage extends BasePage {
    // Локаторы для страницы подписок
    private final String subscriptionPlans = "//div[contains(@class, 'sc-118x72b-0')]//h4[contains(@class, 'sc-56aorr-1')]";
    private final String firstPlan = "(" + subscriptionPlans + ")[1]";
    private final String expandLink = "//div[contains(@class, 'sc-1a5myy-0')][.//h4[contains(text(), 'Basic')]]//button[normalize-space()='Свернуть']";
    private final String moreDetailLink = "//div[contains(@class, 'sc-1a5myy-0')][.//h4[contains(text(), 'Basic')]]//button[normalize-space()='Подробнее']";
    private final String planDescription = firstPlan + "//div[contains(@class, 'plan-description')]";
    private final String planName = "//h4[contains(@class, 'sc-56aorr-1')][contains(., 'Basic')]";
    private final String planPrice = "//h4[contains(text(), 'Basic')]/following::p[contains(@class, 'sc-56aorr-4')][1]";
    private final String buyButton = "//h4[contains(text(), 'Basic')]/ancestor::div[contains(@class, 'sc-1a5myy-0')]//button[text()='Купить']";
    private final String entryBtn = "//div[text()='Вход']";
    private final String subscribeSection = "//div[@class='sc-1a5myy-0 wXPNv'][.//h4[contains(text(), 'Basic')]]";
    private final String email = "//input[@name='email']";
    private final String pass = "//input[@type='password']";
    private final String entriesBtn = "//div[@class='sc-a6ojz8-2' and text()='Войти']";

    public SubscriptionPage(Page page, TestConfig config) {
        super(page, config);
    }

    public void openSubscriptionPage(String path) {
        navigateTo(config.getBaseUrl() + path);
        page.waitForLoadState();
    }

    public int getSubscriptionPlansCount() {
        return page.locator(subscriptionPlans).count();
    }

    public String getFirstPlanExpandLinkText() {
        return page.locator(expandLink).textContent().trim();
    }

    public String getFirstExpandLinkText() {
        return page.locator(moreDetailLink).textContent().trim();
    }

    public void expandFirstSubscriptionDetails() {
        page.locator(moreDetailLink).click();
        page.waitForTimeout(500); // Ждем анимацию
    }

    public void collapseFirstSubscriptionDetails() {
        page.locator(expandLink).click();
        page.waitForTimeout(500); // Ждем анимацию
    }

    public boolean isFirstPlanDescriptionVisible() {
        return page.locator(planDescription).isVisible();
    }

    public String getFirstPlanName() {
        return page.locator(planName).textContent().trim();
    }

    public String getFirstPlanPrice() {
        return page.locator(planPrice).textContent().trim();
    }

    public void clickBuyFirstSubscription() {
        page.locator(buyButton).click();
        page.waitForLoadState();
    }

    public void clickEntryBtn() {
        page.locator(entryBtn).click();
        page.waitForLoadState();
        page.waitForTimeout(3000); // Ждем анимацию
    }

    private void waitForPageToBeReady() {
        page.waitForSelector(subscriptionPlans, new Page.WaitForSelectorOptions().setTimeout(10000));
    }

    public void scrollToElement() {
        page.locator(subscribeSection).scrollIntoViewIfNeeded();
    }

    public void fillFieldEmailByXpath() {
        page.locator(email)
                .fill("budaragindenis@mail.ru",
                        new Locator.FillOptions().setForce(true));
    }
    public void fillFieldPassByXpath() {
        page.locator(pass)
                .fill("JFX478slkQ!",
                        new Locator.FillOptions().setForce(true));
    }

    public void clickEntriesBtn() {
        page.locator(entriesBtn).click();
        page.waitForTimeout(2000);
    }
}
