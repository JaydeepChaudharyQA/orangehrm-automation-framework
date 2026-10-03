package io.github.jaydeepchaudharyqa.orangehrm.pages.pim;

import io.github.jaydeepchaudharyqa.orangehrm.models.Employee;
import io.github.jaydeepchaudharyqa.orangehrm.pages.AuthenticatedPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** PIM → Add Employee form. */
public class AddEmployeePage extends AuthenticatedPage {

    private static final By FIRST_NAME = By.name("firstName");
    private static final By MIDDLE_NAME = By.name("middleName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID =
            By.xpath("//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SAVE_BUTTON = By.cssSelector("button[type='submit']");
    private static final By FIELD_ERRORS = By.cssSelector(".oxd-input-field-error-message");

    public AddEmployeePage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    @Override
    protected String urlFragment() {
        return "/pim/addEmployee";
    }

    @Override
    protected By pageIdentifier() {
        return FIRST_NAME;
    }

    /** Fills the form from an {@link Employee}; blank optional fields are skipped. */
    public AddEmployeePage fillDetails(Employee employee) {
        log.info("Filling new employee: {}", employee);
        actions.type(FIRST_NAME, employee.getFirstName());
        if (!employee.getMiddleName().isEmpty()) {
            actions.type(MIDDLE_NAME, employee.getMiddleName());
        }
        actions.type(LAST_NAME, employee.getLastName());
        if (employee.getEmployeeId() != null) {
            // OrangeHRM pre-fills an id asynchronously; wait for it, then overwrite it.
            waits.valueNotEmpty(EMPLOYEE_ID);
            actions.type(EMPLOYEE_ID, employee.getEmployeeId());
        }
        return this;
    }

    /** Saves a valid form and lands on the new employee's profile. */
    public PersonalDetailsPage save() {
        actions.click(SAVE_BUTTON);
        return new PersonalDetailsPage(driver);
    }

    /** Saves a form that should fail validation, staying on this page. */
    public AddEmployeePage saveExpectingErrors() {
        actions.click(SAVE_BUTTON);
        return this;
    }

    public List<String> getFieldErrors() {
        return actions.getTexts(FIELD_ERRORS);
    }
}
