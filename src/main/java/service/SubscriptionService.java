package service;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;
import org.assertj.core.api.Assertions;
import pages.PaymentPage;
import pages.SubscriptionPage;

public class SubscriptionService {
    private final Page page;
    private final TestConfig config;
    private final SubscriptionPage subscriptionPage;
    private final PaymentPage paymentPage;

    @Inject
    public SubscriptionService(Page page, TestConfig config) {
        this.page = page;
        this.config = config;
        this.subscriptionPage = new SubscriptionPage(page, config);
        this.paymentPage = new PaymentPage(page, config);
    }

    public void openSubscriptionPage() {
        System.out.println("Открытие страницы подписок");
        subscriptionPage.openSubscriptionPage("/subscription");
    }

    public void scrollToSubscribeSection() {
        subscriptionPage.scrollToElement();
    }

    public void verifySubscriptionPlansDisplayed() {
        System.out.println("Проверка отображения вариантов подписок");
        int plansCount = subscriptionPage.getSubscriptionPlansCount();

        Assertions.assertThat(plansCount)
                .as("Должны отображаться варианты подписок")
                .isGreaterThan(0);

        System.out.println("✓ Отображается " + plansCount + " вариантов подписки");
    }

    public void expandFirstSubscriptionDetails() {
        System.out.println("Клик на 'Подробнее' для первой подписки");

        // Раскрываем детали
        subscriptionPage.expandFirstSubscriptionDetails();

        // Проверяем, что ссылка изменилась на "Свернуть"
        String expandedLinkText = subscriptionPage.getFirstPlanExpandLinkText();

        Assertions.assertThat(expandedLinkText)
                .as("Ссылка должна измениться на 'Свернуть'")
                .isEqualTo("Свернуть");
    }

    public void collapseFirstSubscriptionDetails() {
        System.out.println("Клик на 'Свернуть' для первой подписки");

        // Сворачиваем детали
        subscriptionPage.collapseFirstSubscriptionDetails();

        // Проверяем, что ссылка изменилась обратно на "Подробнее"
        String collapsedLinkText = subscriptionPage.getFirstExpandLinkText();

        Assertions.assertThat(collapsedLinkText)
                .as("Ссылка должна измениться на 'Подробнее'")
                .isEqualTo("Подробнее");
    }

    public void purchaseFirstSubscription() {
        System.out.println("Нажатие на кнопку 'Купить' для первой подписки");

        // Сохраняем информацию о подписке для проверок
        String planName = subscriptionPage.getFirstPlanName();
        String planPrice = subscriptionPage.getFirstPlanPrice();

        System.out.println("Выбран тариф: " + planName + " по цене: " + planPrice);

        // Нажимаем кнопку покупки
        subscriptionPage.clickBuyFirstSubscription();

        //Нажимаем вход
        subscriptionPage.clickEntryBtn();

        //Заполняем поля
        //Почта
        subscriptionPage.fillFieldEmailByXpath();
        //Пароль
        subscriptionPage.fillFieldPassByXpath();
        //Нажать Войти
        subscriptionPage.clickEntriesBtn();

        // Ждем загрузки страницы оплаты
        page.waitForLoadState();

        // Проверяем, что мы на странице оплаты
        String currentUrl = page.url();
        Assertions.assertThat(currentUrl)
                .as("Должна открыться страница оплаты")
                .contains("subscription");

        System.out.println("✓ Открыта страница оплаты: " + currentUrl);
    }

    public void verifyPaymentPage() {
        System.out.println("Проверка страницы оплаты");

        // Проверяем, что стоимость отображается
        boolean isPriceDisplayed = paymentPage.isPriceDisplayed();

        Assertions.assertThat(isPriceDisplayed)
                .as("На странице оплаты должна отображаться стоимость")
                .isTrue();
    }

    public void selectTrialSubscription() {
        System.out.println("Выбор пробной подписки (Trial)");

        // Получаем начальную стоимость и продолжительность
        String initialPrice = paymentPage.getPrice();

        System.out.println("До выбора Trial: цена = " + initialPrice);

        // Выбираем Trial подписку
        paymentPage.selectTrialSubscription();

        // Ждем обновления цены
        page.waitForTimeout(1000);

        // Получаем новую стоимость и продолжительность
        String updatedPrice = paymentPage.getPrice();

        System.out.println("После выбора Trial: цена = " + updatedPrice);

        // Проверяем, что цена изменилась
        Assertions.assertThat(updatedPrice)
                .as("Цена должна измениться после выбора Trial подписки")
                .isNotEqualTo(initialPrice);
    }
}
