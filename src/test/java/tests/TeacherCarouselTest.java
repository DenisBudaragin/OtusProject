package tests;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;
import di.DependencyInitializer;
import service.TeacherCarouselService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(PlaywrightExtension.class)
public class TeacherCarouselTest {
    private TeacherCarouselService teacherCarouselService;
    private Page page;

    @BeforeAll
    public void setUp() {
        // Получаем зависимости напрямую через DependencyInitializer
        teacherCarouselService = DependencyInitializer.getInstance(TeacherCarouselService.class);
        page = DependencyInitializer.getInstance(Page.class);
    }

    @Test
    @DisplayName("Проверка карусели преподавателей")
    public void testTeacherCarousel() {
        // Шаг 1: Открыть страницу урока
        teacherCarouselService.openLessonPage("/lessons/clickhouse/");

        // Шаг 2: Проскроллить до раздела преподаватели
        teacherCarouselService.scrollToTeachersSection();

        // Шаг 3: Проверить отображение плиток преподавателей
        teacherCarouselService.verifyTeachersSectionIsDisplayed();
        teacherCarouselService.verifyVisibleTeachersCount();

        // Шаг 4: Прокрутить карусель
        teacherCarouselService.scrollTeachersCarousel();
        teacherCarouselService.verifyCarouselScrolled();

        // Шаг 5: Открыть попап преподавателя
        teacherCarouselService.openTeacherPopup(0);
        teacherCarouselService.verifyPopupTeacherMatchesScrolledTeacher();

        // Шаг 6: Перейти к следующему преподавателю в попапе
        teacherCarouselService.navigateToNextTeacherInPopup();
        teacherCarouselService.verifyNextTeacherIsDifferent();

        // Шаг 7: Вернуться к предыдущему преподавателю в попапе
        teacherCarouselService.navigateToPreviousTeacherInPopup();
        teacherCarouselService.verifyPreviousTeacherIsOriginal();
    }

    @AfterAll
    public void tearDown() {
        if (page != null && page.context() != null && page.context().browser() != null) {
            page.context().browser().close();
        }
    }

}
