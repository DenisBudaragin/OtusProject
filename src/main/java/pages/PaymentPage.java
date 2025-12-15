package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import config.TestConfig;

public class PaymentPage extends BasePage {
    // Локаторы для страницы оплаты
    private final String priceElementOnSubscriptionPage = "//div[@class='Typography-sc-1x9oq14-0 styles__Price-sc-194geb0-1 fdKuiz iazCwa']";
    private final String subscriptionTypeSelect = "//input[@id='radio-base-trial']";
    private final String subscriptionDuration = "//h4[contains(@class, 'Typography') and contains(text(), 'месяц')]/text()";

    public PaymentPage(Page page, TestConfig config) {
        super(page, config);
    }

    public boolean isPriceDisplayed() {
        Locator priceLocator = page.locator(priceElementOnSubscriptionPage);
        priceLocator.waitFor(new Locator.WaitForOptions().setTimeout(3000));
        return priceLocator.isVisible();
    }

    public String getPrice() {
        return page.locator(priceElementOnSubscriptionPage).textContent().trim();
    }

    public void selectTrialSubscription() {
        // Выбираем Trial option из select
        page.locator(subscriptionTypeSelect).click();
        page.waitForTimeout(1000); // Ждем обновления цены
    }

    public String getSubscriptionDuration() {
        return page.locator(subscriptionDuration).textContent().trim();
    }
}
