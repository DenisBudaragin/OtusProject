package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import config.TestConfig;

public class CatalogPage extends BasePage{
    // Локаторы
    private final String directionFilterButton = "//div[@value and .//label[text()='Все направления']]";
    private final String complexityFilterButton = "//div[@value and .//label[text()='Любой уровень']]";
    private final String resetFilterButton = "//button[contains(text(), 'Очистить фильтры')]";
    private final String minSliderLocator = "//div[@role='slider' and @aria-valuenow='0']";
    private final String maxSliderLocator = "//div[@role='slider' and @aria-valuenow='15']";
    private final String architectureDirection = "//div[@value and .//label[text()='Архитектура']]";
    private final String courseCards = "//div[@class='sc-18q05a6-1 bwGwUO']//a[contains(@class, 'sc-zzdkm7-0')]";

    public CatalogPage(Page page, TestConfig config) {
        super(page, config);
    }

    public void openCatalogPage(String path) {
        navigateTo(config.getBaseUrl() + path);
        page.waitForLoadState();
    }

    public void verifyDefaultFilters() {
        // Проверяем, что выбраны "Все направления"
        Locator directionFilter = page.locator(directionFilterButton);
        String valueAllDirection = directionFilter.getAttribute("value");
        String directionText = directionFilter.textContent().trim();
        if (!directionText.contains("Все направления") && !valueAllDirection.contains("true")) {
            throw new AssertionError("Фильтр направлений должен быть 'Все направления', а выбрано: " + directionText);
        }

        // Проверяем, что выбран "Любой уровень сложности"
        Locator complexityFilter = page.locator(complexityFilterButton);
        String valueAnyLevel = directionFilter.getAttribute("value");
        String complexityText = complexityFilter.textContent().trim();
        if (!complexityText.contains("Любой уровень сложности") && !valueAnyLevel.contains("true")) {
            throw new AssertionError("Фильтр сложности должен быть 'Любой уровень сложности', а выбрано: " + complexityText);
        }
    }

    public void setDurationFilter(int minMonths, int maxMonths) {
        // Ждем появления ползунков
        page.waitForSelector(minSliderLocator);
        page.waitForSelector(maxSliderLocator);

        // Получаем ползунки
        Locator minSlider = page.locator(minSliderLocator);
        Locator maxSlider = page.locator(maxSliderLocator);

        // Проверяем начальные позиции
        String initialMinValue = minSlider.getAttribute("aria-valuenow");
        String initialMaxValue = maxSlider.getAttribute("aria-valuenow");
        System.out.println("Начальные значения ползунков: min=" + initialMinValue + ", max=" + initialMaxValue);

        // Получаем размеры и позицию трека ползунка
        Locator sliderTrack = page.locator("//div[contains(@class, 'sc-1kvgqt9-1')]");
        BoundingBox trackBox = sliderTrack.boundingBox();

        if (trackBox == null) {
            throw new RuntimeException("Не удалось найти трек ползунка");
        }

        // Вычисляем позиции для ползунков
        int minPosition = calculateSliderPosition(trackBox, minMonths, 0, 15);
        int maxPosition = calculateSliderPosition(trackBox, maxMonths, 0, 15);

        System.out.println("Целевые позиции: min=" + minPosition + "px, max=" + maxPosition + "px");

        // Перемещаем минимальный ползунок
        dragSliderToPosition(minSlider, trackBox, minPosition, true);

        // Ждем обновления значения
        page.waitForTimeout(3000);

        // Перемещаем максимальный ползунок
        dragSliderToPosition(maxSlider, trackBox, maxPosition, false);

        // Ждем обновления значения
        page.waitForTimeout(3000);
        System.out.println("✓ Фильтр продолжительности установлен на " + minMonths + "-" + maxMonths + " месяцев");
    }

    private int calculateSliderPosition(BoundingBox trackBox, int value, int minValue, int maxValue) {
        // Вычисляем позицию на основе значения и диапазона
        double percentage = (double) (value - minValue) / (maxValue - minValue);
        int position = (int) (trackBox.x + trackBox.width * percentage);

        return position;
    }

    private void dragSliderToPosition(Locator slider, BoundingBox trackBox, int targetX, boolean isMinSlider) {
        // Получаем текущую позицию ползунка
        BoundingBox sliderBox = slider.boundingBox();
        if (sliderBox == null) {
            throw new RuntimeException("Не удалось получить позицию ползунка");
        }

        // Определяем смещение для точного клика по центру ползунка
        int sliderCenterX = (int) (sliderBox.x + sliderBox.width / 2);

        // Вычисляем смещение для перетаскивания
        int offsetX = targetX - sliderCenterX;

        // Проверяем, что целевая позиция находится в пределах трека
        if (targetX < trackBox.x) {
            targetX = (int) trackBox.x;
        } else if (targetX > trackBox.x + trackBox.width) {
            targetX = (int) (trackBox.x + trackBox.width);
        }

        System.out.println("Перемещение ползунка с " + sliderCenterX + "px на " + targetX + "px (смещение: " + offsetX + "px)");

        // Выполняем drag-and-drop
        try {
            // Нажимаем на ползунок
            slider.hover();
            page.mouse().down();
            page.waitForTimeout(200);

            // Перемещаем мышь на целевую позицию
            page.mouse().move(sliderCenterX + offsetX, (int) sliderBox.y);
            page.waitForTimeout(200);

            // Отпускаем мышь
            page.mouse().up();
            page.waitForTimeout(200);

        } catch (Exception e) {
            System.err.println("Ошибка при перетаскивании ползунка: " + e.getMessage());

            // Альтернативный способ - использование dragTo
            try {
                // Находим точку назначения (по Y берем центр трека)
                int targetY = (int) (trackBox.y + trackBox.height / 2);

                if (isMinSlider) {
                    // Для минимального ползунка используем метод dragTo
                    slider.dragTo(page.locator("body"),
                            new Locator.DragToOptions()
                                    .setTargetPosition(targetX, targetY));
                } else {
                    // Для максимального ползунка
                    slider.dragTo(page.locator("body"),
                            new Locator.DragToOptions()
                                    .setTargetPosition(targetX, targetY));
                }
                page.waitForTimeout(200);

            } catch (Exception e2) {
                System.err.println("Альтернативный способ также не сработал: " + e2.getMessage());
            }
        }
    }

    public void scrollToElement(String text) {
        // Локатор для элемента с текстом "Продолжительность"
        Locator element = page.locator("//p[text()='" + text + "']")
                .filter(new Locator.FilterOptions().setHasText(text));

        element.scrollIntoViewIfNeeded();
    }

    public void selectArchitectureDirection() {
        page.locator(architectureDirection).click();
        page.waitForTimeout(1000);
    }

    public int getVisibleCoursesCount() {
        return page.locator(courseCards).count();
    }

    public void resetFilters() {
        page.locator(resetFilterButton).click();
        page.waitForTimeout(1000);
    }

    public String getFirstCourseTitle() {
        Locator firstCard = page.locator(courseCards).first();
        firstCard.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return firstCard.textContent().trim();
    }
}
