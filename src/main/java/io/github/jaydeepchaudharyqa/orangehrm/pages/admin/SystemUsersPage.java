package io.github.jaydeepchaudharyqa.orangehrm.pages.admin;

import io.github.jaydeepchaudharyqa.orangehrm.pages.BaseListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** Admin → User Management → System Users: search and filter user accounts. */
public class SystemUsersPage extends BaseListPage {

    private static final By ROLE_DROPDOWN = By.xpath(
            "//label[normalize-space()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text')]");
    private static final String DROPDOWN_OPTION =
            "//div[@role='listbox']//div[@role='option'][normalize-space()='%s']";

    private static final int COL_USERNAME = 1;
    private static final int COL_ROLE = 2;
    private static final int COL_STATUS = 4;

    public SystemUsersPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    @Override
    protected String urlFragment() {
        return "/admin/viewSystemUsers";
    }

    @Override
    protected By pageIdentifier() {
        return filterInput("Username");
    }

    public SystemUsersPage searchByUsername(String username) {
        log.info("Searching system users by username '{}'", username);
        actions.type(filterInput("Username"), username);
        clickSearch();
        return this;
    }

    /** Filters by role, e.g. "Admin" or "ESS". */
    public SystemUsersPage filterByRole(String role) {
        log.info("Filtering system users by role '{}'", role);
        actions.click(ROLE_DROPDOWN);
        actions.click(By.xpath(String.format(DROPDOWN_OPTION, role)));
        clickSearch();
        return this;
    }

    public List<String> getUsernames() {
        return getColumnValues(COL_USERNAME);
    }

    public List<String> getRoles() {
        return getColumnValues(COL_ROLE);
    }

    public List<String> getStatuses() {
        return getColumnValues(COL_STATUS);
    }
}
