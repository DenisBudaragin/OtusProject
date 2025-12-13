package service;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import config.TestConfig;
import pages.LessonPage;
import pages.TeacherPopup;
import org.assertj.core.api.Assertions;

public class TeacherCarouselService {
    private final Page page;
    private final TestConfig config;
    private final LessonPage lessonPage;
    private final TeacherPopup teacherPopup;

    private String firstTeacherBeforeScroll;
    private String firstTeacherAfterScroll;
    private String teacherNameInPopup;
    private String nextTeacherName;
    private String previousTeacherName;

    @Inject
    public TeacherCarouselService(Page page, TestConfig config) {
        this.page = page;
        this.config = config;
        this.lessonPage = new LessonPage(page, config);
        this.teacherPopup = new TeacherPopup(page, config);
    }

    public void openLessonPage(String path) {
        System.out.println("Открытие страницы урока: " + path);
        lessonPage.openLessonPage(path);
    }

    public void scrollToTeachersSection() {
        lessonPage.scrollToElement();
    }

    public void verifyTeachersSectionIsDisplayed() {
        boolean isDisplayed = lessonPage.isTeachersSectionDisplayed();
        Assertions.assertThat(isDisplayed)
                .as("Секция преподавателей должна отображаться")
                .isTrue();
    }

    public void verifyVisibleTeachersCount() {
        int teacherCount = lessonPage.getVisibleTeachersCount();
        Assertions.assertThat(teacherCount)
                .as("Должно отображаться хотя бы 2 преподавателя")
                .isGreaterThan(1);
        System.out.println("Найдено преподавателей: " + teacherCount);
    }

    public void scrollTeachersCarousel() {
        System.out.println("Прокрутка карусели преподавателей");
        firstTeacherBeforeScroll = lessonPage.getFirstVisibleTeacherName();
        System.out.println("Первый преподаватель до прокрутки: " + firstTeacherBeforeScroll);

        lessonPage.scrollTeachersCarousel();
        page.waitForTimeout(1000);

        firstTeacherAfterScroll = lessonPage.getFirstVisibleTeacherName();
        System.out.println("Первый преподаватель после прокрутки: " + firstTeacherAfterScroll);
    }

    public void verifyCarouselScrolled() {
        Assertions.assertThat(firstTeacherAfterScroll)
                .as("Список преподавателей должен прокрутиться")
                .isNotEqualTo(firstTeacherBeforeScroll);
    }

    public void openTeacherPopup(int index) {
        System.out.println("Открытие попапа преподавателя");
        lessonPage.clickTeacherCard(index);

        boolean popupVisible = teacherPopup.waitForPopupToBeVisible();
        Assertions.assertThat(popupVisible)
                .as("Попап преподавателя должен открыться")
                .isTrue();

        teacherNameInPopup = teacherPopup.getTeacherNameAfterPopUpOpened(2);
        System.out.println("Имя преподавателя в попапе: " + teacherNameInPopup);
    }

    public void verifyPopupTeacherMatchesScrolledTeacher() {
        Assertions.assertThat(teacherNameInPopup)
                .as("Имя преподавателя в попапе должно совпадать с именем после прокрутки")
                .isEqualToIgnoringCase(firstTeacherAfterScroll);
    }

    public void navigateToNextTeacherInPopup() {
        System.out.println("Переход к следующему преподавателю в попапе");
        teacherPopup.clickPopUpButton(2);

        nextTeacherName = teacherPopup.getTeacherNameAfterPopUpOpened(3);
        System.out.println("Следующий преподаватель: " + nextTeacherName);
    }

    public void verifyNextTeacherIsDifferent() {
        Assertions.assertThat(nextTeacherName)
                .as("Должен открыться другой преподаватель")
                .isNotEqualTo(teacherNameInPopup);
    }

    public void navigateToPreviousTeacherInPopup() {
        System.out.println("Возврат к предыдущему преподавателю в попапе");
        teacherPopup.clickPopUpButton(1);

        previousTeacherName = teacherPopup.getTeacherNameAfterPopUpOpened(2);
        System.out.println("Предыдущий преподаватель: " + previousTeacherName);
    }

    public void verifyPreviousTeacherIsOriginal() {
        Assertions.assertThat(previousTeacherName)
                .as("Должен вернуться оригинальный преподаватель")
                .isEqualTo(teacherNameInPopup);
    }
}
