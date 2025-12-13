//package tests;
//import com.google.inject.Inject;
//import com.microsoft.playwright.Page;
//import config.TestConfig;
//import di.DependencyInitializer;
//import pages.LessonPage;
//import pages.TeacherPopup;
//import org.assertj.core.api.Assertions;
//
//public class TeacherCarouselTest {
//    private final Page page;
//    private final TestConfig config;
//    private final LessonPage lessonPage;
//    private final TeacherPopup teacherPopup;
//
//    @Inject
//    public TeacherCarouselTest(Page page, TestConfig config) {
//        this.page = page;
//        this.config = config;
//        this.lessonPage = new LessonPage(page, config);
//        this.teacherPopup = new TeacherPopup(page, config);
//    }
//
//    public void runTest() {
//        try {
//            System.out.println("Запуск теста: Проверка карусели преподавателей");
//
//            // Шаг 1: Открыть страницу
//            System.out.println("Шаг 1: Открытие страницы урока");
//            lessonPage.openLessonPage("/lessons/clickhouse/");
//
//            //Проскроллить до раздела преподаватели
//            lessonPage.scrollToElement();
//
//            // Шаг 2: Проверить отображение плиток преподавателей
//            System.out.println("Шаг 2: Проверка отображения преподавателей");
//            Assertions.assertThat(lessonPage.isTeachersSectionDisplayed())
//                    .as("Секция преподавателей должна отображаться")
//                    .isTrue();
//
//            int initialTeacherCount = lessonPage.getVisibleTeachersCount();
//            Assertions.assertThat(initialTeacherCount)
//                    .as("Должно отображаться хотя бы 2 преподавателя")
//                    .isGreaterThan(1);
//
//            System.out.println("Найдено преподавателей: " + initialTeacherCount);
//
//            // Шаг 3: Прокрутить карусель с помощью drag and drop
//            System.out.println("Шаг 3: Прокрутка карусели преподавателей");
//            String firstTeacherBeforeScroll = lessonPage.getFirstVisibleTeacherName();
//            System.out.println("Первый преподаватель до прокрутки: " + firstTeacherBeforeScroll);
//
//            lessonPage.scrollTeachersCarousel();
//
//            // Небольшая пауза для завершения анимации
//            page.waitForTimeout(1000);
//
//            String firstTeacherAfterScroll = lessonPage.getFirstVisibleTeacherName();
//            System.out.println("Первый преподаватель после прокрутки: " + firstTeacherAfterScroll);
//
//            //Шаг 4: Проверить, что список прокрутился
//            System.out.println("Шаг 4: Проверка прокрутки карусели");
//            Assertions.assertThat(firstTeacherAfterScroll)
//                    .as("Список преподавателей должен прокрутиться")
//                    .isNotEqualTo(firstTeacherBeforeScroll);
//
//            // Шаг 5: Кликнуть на плитку преподавателя
//            System.out.println("Шаг 5: Открытие попапа преподавателя");
//            lessonPage.clickTeacherCard(0);
//
//            boolean popupVisible = teacherPopup.waitForPopupToBeVisible();
//            Assertions.assertThat(popupVisible)
//                    .as("Попап преподавателя должен открыться")
//                    .isTrue();
//
//            String teacherNameInPopup = teacherPopup.getTeacherNameAfterPopUpOpened(2);
//            System.out.println("Имя преподавателя в попапе: " + teacherNameInPopup);
//
//            //Проверить что имя препода после скролла вправо равно имени после открытия поп-апа
//            Assertions.assertThat(teacherNameInPopup)
//                    .as("Строки должны совпадать (игнорируя регистр)")
//                    .isEqualToIgnoringCase(firstTeacherAfterScroll);
//
//
//
//            // Шаг 6: Нажать на кнопку ">" для перехода к следующему преподавателю
//            System.out.println("Шаг 6: Переход к следующему преподавателю");
//            teacherPopup.clickPopUpButton(2);
//
//            String nextTeacherName = teacherPopup.getTeacherNameAfterPopUpOpened(3);
//            System.out.println("Следующий преподаватель: " + nextTeacherName);
//
//            Assertions.assertThat(nextTeacherName)
//                    .as("Должен открыться другой преподаватель")
//                    .isNotEqualTo(teacherNameInPopup);
//
//            // Шаг 7: Нажать на кнопку "<" для перехода к предыдущему преподавателю
//            System.out.println("Шаг 7: Возврат к предыдущему преподавателю");
//            teacherPopup.clickPopUpButton(1);
//
//            String previousTeacherName = teacherPopup.getTeacherNameAfterPopUpOpened(2);
//            System.out.println("Предыдущий преподаватель: " + previousTeacherName);
//
//            Assertions.assertThat(previousTeacherName)
//                    .as("Должен вернуться предыдущий преподаватель")
//                    .isEqualTo(teacherNameInPopup);
//
//            System.out.println("Тест успешно завершен!");
//
//        } catch (Exception e) {
//            System.err.println("Ошибка при выполнении теста: " + e.getMessage());
//            e.printStackTrace();
//            throw e;
//        } finally {
//            // Закрытие страницы и браузера выполнится в shutdown hook
//            System.out.println("Завершение работы...");
//        }
//    }
//
//    public static void main(String[] args) {
//        // Инициализация DI и запуск теста
//        TeacherCarouselTest test = DependencyInitializer.getInstance(TeacherCarouselTest.class);
//
//        // Добавляем shutdown hook для корректного закрытия ресурсов
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("Закрытие браузера...");
//            test.page.context().browser().close();
////            test.page.context().browser().playwright().close();
//        }));
//
//        try {
//            test.runTest();
//            System.exit(0);
//        } catch (Exception e) {
//            System.err.println("Тест завершился с ошибкой");
//            System.exit(1);
//        }
//    }
//}
