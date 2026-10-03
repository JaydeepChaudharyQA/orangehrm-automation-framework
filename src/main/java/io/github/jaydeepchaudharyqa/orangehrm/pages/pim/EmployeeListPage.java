package io.github.jaydeepchaudharyqa.orangehrm.pages.pim;

import io.github.jaydeepchaudharyqa.orangehrm.pages.BaseListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** PIM → Employee List: search, view and delete employees. */
public class EmployeeListPage extends BaseListPage {

    private static final By ADD_BUTTON = By.xpath("//div[contains(@class,'orangehrm-header-container')]//button[normalize-space()='Add']");

    private static final int COL_ID = 1;
    private static final int COL_FIRST_MIDDLE_NAME = 2;
    private static final int COL_LAST_NAME = 3;

    public EmployeeListPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    @Override
    protected String urlFragment() {
        return "/pim/viewEmployeeList";
    }

    @Override
    protected By pageIdentifier() {
        return ADD_BUTTON;
    }

    public AddEmployeePage clickAdd() {
        actions.click(ADD_BUTTON);
        return new AddEmployeePage(driver);
    }

    public EmployeeListPage searchByEmployeeId(String employeeId) {
        log.info("Searching employees by id {}", employeeId);
        actions.type(filterInput("Employee Id"), employeeId);
        clickSearch();
        return this;
    }

    public List<String> getEmployeeIds() {
        return getColumnValues(COL_ID);
    }

    public List<String> getFirstAndMiddleNames() {
        return getColumnValues(COL_FIRST_MIDDLE_NAME);
    }

    public List<String> getLastNames() {
        return getColumnValues(COL_LAST_NAME);
    }

    /** Deletes the first employee in the results and returns the toast message. */
    public String deleteFirstResult() {
        log.info("Deleting the first employee in the results");
        return deleteRow(1);
    }
}
