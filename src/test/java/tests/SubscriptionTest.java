package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import extension.PlayWrightExtensions;
import org.junit.jupiter.api.TestInstance;
import service.SubscriptionService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(PlayWrightExtensions.class)
public class SubscriptionTest {
    @Test
    @DisplayName("Проверка функционала подписок и оплаты")
    public void testSubscriptionPurchaseFlow(SubscriptionService subscriptionService) {
        subscriptionService.openSubscriptionPage();
        subscriptionService.scrollToSubscribeSection();
        subscriptionService.verifySubscriptionPlansDisplayed();
        subscriptionService.expandFirstSubscriptionDetails();
        subscriptionService.collapseFirstSubscriptionDetails();
        subscriptionService.purchaseFirstSubscription();
        subscriptionService.verifyPaymentPage();
        subscriptionService.selectTrialSubscription();
    }
}
