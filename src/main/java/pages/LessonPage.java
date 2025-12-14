package pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LessonPage extends BasePage {
    // Локаторы
    private final String teachersSection = "//*[text()='Преподаватели']";
    private final String teacherCard = "//div[@class='sc-jotj87-1 fjUCpx']";
    private final String teacherName = "//p[@class='sc-1x9oq14-0 sc-1s527z5-1 gGtEnS iiYkXk']";
    private final String teacherCaruselNextButton = "//button[@class='sc-1bkbgbz-2 sc-1bkbgbz-3 dQlnjC iPzpLW']";

    public LessonPage(Page page, TestConfig config) {
        super(page, config);
    }

    public void openLessonPage(String lessonPath) {
        navigateTo(config.getBaseUrl() + lessonPath);
    }

    public boolean isTeachersSectionDisplayed() {
        return page.locator(teachersSection).isVisible();
    }

    public void scrollToElement() {
        page.locator(teachersSection).scrollIntoViewIfNeeded();
    }

    public int getVisibleTeachersCount() {
        return page.locator(teacherCard).count();
    }

    public void scrollTeachersCarousel() {
        Logger logger = LoggerFactory.getLogger(getClass());

        logger.info("Начинаем клик по кнопке карусели");
        logger.info("Локатор: {}", teacherCaruselNextButton);

        try {
            Locator carouselButton = page.locator(teacherCaruselNextButton);

            logger.info("Проверяем наличие кнопки...");
            int buttonCount = carouselButton.count();
            logger.info("Найдено элементов: {}", buttonCount);

            if (buttonCount > 0) {
                logger.info("Проверяем видимость кнопки...");
                boolean isVisible = carouselButton.isVisible();
                logger.info("Кнопка видима: {}", isVisible);

                if (isVisible) {
                    logger.info("Проверяем активность кнопки...");
                    boolean isEnabled = carouselButton.isEnabled();
                    logger.info("Кнопка активна: {}", isEnabled);

                    if (isEnabled) {
                        logger.info("Выполняем клик по кнопке...");
                        carouselButton.click();
                        carouselButton.click();
                        carouselButton.click();
                        logger.info("✅ Клик успешно выполнен");

                        // Ожидание после клика
                        page.waitForTimeout(500);
                        logger.info("Ожидание завершено (500мс)");
                    } else {
                        logger.warn("Кнопка неактивна (disabled)");
                        throw new RuntimeException("Кнопка карусели неактивна");
                    }
                } else {
                    logger.warn("Кнопка не видна на экране");
                    logger.info("Пытаемся прокрутить до кнопки...");
                    carouselButton.scrollIntoViewIfNeeded();
                    page.waitForTimeout(1000);

                    // Повторная проверка видимости
                    if (carouselButton.isVisible()) {
                        logger.info("Теперь кнопка видна, выполняем клик...");
                        carouselButton.click();
                        logger.info("✅ Клик выполнен после скролла");
                    } else {
                        throw new RuntimeException("Не удалось сделать кнопку видимой");
                    }
                }
            } else {
                logger.error("Кнопка не найдена по указанному локатору");
                throw new RuntimeException("Элемент не найден: " + teacherCaruselNextButton);
            }

        } catch (Exception e) {
            logger.error("❌ Ошибка при клике по кнопке:", e);
            throw e;
        }
    }

    public String getFirstVisibleTeacherName() {
        return page.locator(teacherName).first().textContent().trim();
    }

    public void clickTeacherCard(int index) {
        page.locator(teacherCard).nth(index).click();
    }
}
