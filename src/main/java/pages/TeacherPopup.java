package pages;
import com.microsoft.playwright.Page;
import config.TestConfig;

public class TeacherPopup extends BasePage {
    public String teacherName = null;
    // Локаторы
    private final String popupContainer = "//div[contains(@class, 'swiper-slide-active')]";
    private final String teacherNameInPopup = "//h3[normalize-space(@class)='sc-1x9oq14-0 jmLQpp']";
    private final String teacherNameInPopupForCompare = "//h3[text()=" + "'" + teacherName + "']";
    private final String nextButton = popupContainer + "//button[@class='sc-1bkbgbz-2 sc-1bkbgbz-3 dQreKk iPzpLW']";
    private final String prevButton = popupContainer + "//button[contains(@class, 'prev-button')]";
    private final String closeButton = popupContainer + "//button[contains(@class, 'close-button')]";

    public TeacherPopup(Page page, TestConfig config) {
        super(page, config);
    }

    public boolean isPopupVisible() {
        return page.locator(popupContainer).isVisible();
    }

    public String getTeacherNameInPopup() {
        return page.locator(teacherNameInPopup).textContent().trim();
    }

    public String getTeacherNameAfterPopUpOpened(int index) {
        return page.locator("(//h3[@class='sc-1x9oq14-0 jmLQpp'])[" + index + "]").textContent().trim();
    }


    public void clickPopUpButton(int index) {
        page.locator("(//button[contains(@class, 'dQreKk')])[" + index +"]").click();
        page.waitForTimeout(300); // Ждем анимацию перехода
    }

    public void clickPrevButton() {
        page.locator(prevButton).click();
        page.waitForTimeout(300); // Ждем анимацию перехода
    }

    public void closePopup() {
        page.locator(closeButton).click();
    }

    public boolean waitForPopupToBeVisible() {
        try {
            page.waitForSelector(popupContainer,
                    new Page.WaitForSelectorOptions().setTimeout(5000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
