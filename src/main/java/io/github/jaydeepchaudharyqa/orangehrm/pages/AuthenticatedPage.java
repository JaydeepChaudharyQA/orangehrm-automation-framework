package io.github.jaydeepchaudharyqa.orangehrm.pages;

import io.github.jaydeepchaudharyqa.orangehrm.pages.admin.SystemUsersPage;
import io.github.jaydeepchaudharyqa.orangehrm.pages.components.SideMenu;
import io.github.jaydeepchaudharyqa.orangehrm.pages.components.TopBar;
import io.github.jaydeepchaudharyqa.orangehrm.pages.pim.EmployeeListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Parent of every page reached after login. These pages share the top bar and side menu,
 * which are modelled as reusable components (composition) rather than repeated locators.
 */
public abstract class AuthenticatedPage extends BasePage {

    private static final By TOAST_MESSAGE = By.cssSelector(".oxd-toast .oxd-text--toast-message");

    private final TopBar topBar;
    private final SideMenu sideMenu;

    protected AuthenticatedPage(WebDriver driver) {
        super(driver);
        this.topBar = new TopBar(driver);
        this.sideMenu = new SideMenu(driver);
    }

    public TopBar topBar() {
        return topBar;
    }

    public SideMenu sideMenu() {
        return sideMenu;
    }

    public String getModuleTitle() {
        return topBar.getModuleTitle();
    }

    /** Text of the pop-up notification, e.g. "Successfully Saved". */
    public String getToastMessage() {
        return waits.textNotEmpty(TOAST_MESSAGE);
    }

    // ---- Typed navigation: tests move between pages without knowing any locators ----

    public DashboardPage goToDashboard() {
        sideMenu.open("Dashboard");
        return new DashboardPage(driver);
    }

    public EmployeeListPage goToPim() {
        sideMenu.open("PIM");
        return new EmployeeListPage(driver);
    }

    public SystemUsersPage goToAdmin() {
        sideMenu.open("Admin");
        return new SystemUsersPage(driver);
    }

    public LoginPage logout() {
        log.info("Logging out");
        topBar.clickLogout();
        return new LoginPage(driver);
    }
}
