package tests;

import extension.PlayWrightExtensions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import service.TimeDurationCoursesService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(PlayWrightExtensions.class)
public class TimeDurationCoursesTest {
    @Test
    @DisplayName("Проверка продолжительности курса")
    public void testTimeDurationCourses(TimeDurationCoursesService timeDurationCoursesService) {
        timeDurationCoursesService.openCatalogPage("/catalog/courses");
        timeDurationCoursesService.verifyDefaultFilterSettings();
        timeDurationCoursesService.scrollTo("Продолжительность");
        timeDurationCoursesService.applyDurationFilter(3, 10);
        timeDurationCoursesService.checkCourseDuration();
        timeDurationCoursesService.selectArchitectureDirection();
        timeDurationCoursesService.resetAllFilters();
    }
}
