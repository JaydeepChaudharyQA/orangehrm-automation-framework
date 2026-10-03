package io.github.jaydeepchaudharyqa.orangehrm.pages.pim;

import io.github.jaydeepchaudharyqa.orangehrm.pages.AuthenticatedPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** An employee's profile (Personal Details tab), shown after saving a new employee. */
public class PersonalDetailsPage extends AuthenticatedPage {

    private static final By EMPLOYEE_NAME_HEADER = By.cssSelector(".orangehrm-edit-employee-name h6");
    private static final By FIRST_NAME = By.name("firstName");
    private static final By LAST_NAME = By.name("lastName");
    private static final By EMPLOYEE_ID =
            By.xpath("//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input");

    public PersonalDetailsPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    @Override
    protected String urlFragment() {
        return "/pim/viewPersonalDetails";
    }

    @Override
    protected By pageIdentifier() {
        return EMPLOYEE_NAME_HEADER;
    }

    /** The "First Last" name in the profile header. */
    public String getDisplayedName() {
        return waits.textNotEmpty(EMPLOYEE_NAME_HEADER);
    }

    public String getFirstName() {
        return waits.valueNotEmpty(FIRST_NAME);
    }

    public String getLastName() {
        return waits.valueNotEmpty(LAST_NAME);
    }

    public String getEmployeeId() {
        return waits.valueNotEmpty(EMPLOYEE_ID);
    }
}
