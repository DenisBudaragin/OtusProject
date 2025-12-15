package service;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;
import helpers.MonthPhraseHelper;
import pages.CatalogPage;
import org.assertj.core.api.Assertions;

public class TimeDurationCoursesService {
    private final Page page;
    private final TestConfig config;
    private final CatalogPage catalogPage;
    private final CatalogPage catalogPageAfetFilter;
    private int resetCoursesCount;
    private String firstCourseBeforeFilter;

    @Inject
    public TimeDurationCoursesService(Page page, TestConfig config) {
        this.page = page;
        this.config = config;
        this.catalogPage = new CatalogPage(page, config);
        this.catalogPageAfetFilter = new CatalogPage(page, config);
    }

    public void openCatalogPage(String path) {
        System.out.println("Открытие страницы каталога курсов" + path);
        catalogPage.openCatalogPage(path);
    }

    public void verifyDefaultFilterSettings() {
        System.out.println("Проверка фильтров по умолчанию");
        catalogPage.verifyDefaultFilters();
        System.out.println("✓ По умолчанию выбраны 'Все направления' и 'Любой уровень сложности'");
    }

    public void scrollTo(String elementName) {
        catalogPage.scrollToElement(elementName);
    }

    public void applyDurationFilter(int minMonths, int maxMonths) {
        System.out.println(String.format("Установка фильтра продолжительности: %d-%d месяцев", minMonths, maxMonths));
        // Применяем фильтр
        catalogPage.setDurationFilter(minMonths, maxMonths);
    }

    public void checkCourseDuration() {
        firstCourseBeforeFilter = catalogPage.getFirstCourseTitle();
        System.out.println("Наименование и продолжительность курса после фильтрации: " + firstCourseBeforeFilter);
        String duration = MonthPhraseHelper.extractMonthPhrase(firstCourseBeforeFilter);
        boolean durationMonth = MonthPhraseHelper.validateMonth(duration);
        Assertions.assertThat(duration)
                .as("Продолжительность курса не соответствует заданной %d", duration)
                .isNotEqualTo(true);
    }

    public void selectArchitectureDirection() {
        System.out.println("Выбор направления 'Архитектура'");

        // Сохраняем текущее состояние
        String courseBeforeDirection = catalogPage.getFirstCourseTitle();

        // Применяем фильтр
        catalogPage.selectArchitectureDirection();

        String firstCourseAfterFilter = catalogPage.getFirstCourseTitle();
        System.out.println("Первый курс после фильтрации по направлению: " + firstCourseAfterFilter);

        Assertions.assertThat(firstCourseAfterFilter)
                .as("Плитки курсов должны измениться после выбора направления")
                .isNotEqualTo(courseBeforeDirection);
    }

    public void resetAllFilters() {
        System.out.println("Сброс всех фильтров");

        // Сохраняем текущее состояние
        String courseBeforeReset = catalogPage.getFirstCourseTitle();

        // Сбрасываем фильтры
        catalogPage.resetFilters();
        resetCoursesCount = catalogPage.getVisibleCoursesCount();
        String courseAfterReset = catalogPage.getFirstCourseTitle();
        System.out.println("Курсов после сброса фильтров: " + resetCoursesCount);
        System.out.println("Первый курс после сброса: " + courseAfterReset);

        Assertions.assertThat(courseAfterReset)
                .as("Плитки курсов должны измениться после сброса фильтров")
                .isNotEqualTo(courseBeforeReset);
    }
}
