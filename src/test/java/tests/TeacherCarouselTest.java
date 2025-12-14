package tests;

import service.TeacherCarouselService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import extension.PlayWrightExtensions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(PlayWrightExtensions.class)
public class TeacherCarouselTest {
    @Test
    @DisplayName("Проверка карусели преподавателей")
    public void testTeacherCarousel(TeacherCarouselService teacherCarouselService) {
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
}
