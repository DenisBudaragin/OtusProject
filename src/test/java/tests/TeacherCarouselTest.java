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
        teacherCarouselService.openLessonPage("/lessons/clickhouse/");

        teacherCarouselService.scrollToTeachersSection();

        teacherCarouselService.verifyTeachersSectionIsDisplayed();
        teacherCarouselService.verifyVisibleTeachersCount();

        teacherCarouselService.scrollTeachersCarousel();
        teacherCarouselService.verifyCarouselScrolled();

        teacherCarouselService.openTeacherPopup(0);
        teacherCarouselService.verifyPopupTeacherMatchesScrolledTeacher();

        teacherCarouselService.navigateToNextTeacherInPopup();
        teacherCarouselService.verifyNextTeacherIsDifferent();

        teacherCarouselService.navigateToPreviousTeacherInPopup();
        teacherCarouselService.verifyPreviousTeacherIsOriginal();
    }
}
