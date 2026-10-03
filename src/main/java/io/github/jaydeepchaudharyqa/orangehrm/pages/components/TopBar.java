package io.github.jaydeepchaudharyqa.orangehrm.pages.components;

import io.github.jaydeepchaudharyqa.orangehrm.exceptions.ElementInteractionException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The header bar shown on every page after login: module title and the user menu. */
public class TopBar extends BaseComponent {

    private static final By MODULE_TITLE = By.cssSelector(".oxd-topbar-header-breadcrumb-module");
    private static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-tab");
    private static final By USER_NAME = By.cssSelector(".oxd-userdropdown-name");
    private static final By LOGOUT_LINK = By.xpath("//ul[contains(@class,'oxd-dropdown-menu')]//a[normalize-space()='Logout']");

    public TopBar(WebDriver driver) {
        super(driver);
    }

    /** The module name in the header, e.g. "Dashboard", "PIM" or "Admin". */
    public String getModuleTitle() {
        return waits.textNotEmpty(MODULE_TITLE);
    }

    /**
     * Waits (up to the explicit timeout) for the header to show {@code expected}, then returns the
     * actual title, so a test assertion reports the real value if navigation went wrong.
     */
    public String waitForModuleTitle(String expected) {
        try {
            waits.textPresent(MODULE_TITLE, expected);
        } catch (ElementInteractionException e) {
            // Fall through: the assertion in the test shows the actual title.
        }
        return getModuleTitle();
    }

    public String getLoggedInUserName() {
        return waits.textNotEmpty(USER_NAME);
    }

    public void openUserMenu() {
        actions.click(USER_DROPDOWN);
    }

    public void clickLogout() {
        openUserMenu();
        actions.click(LOGOUT_LINK);
    }
}
