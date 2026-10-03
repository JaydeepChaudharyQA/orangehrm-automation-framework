package io.github.jaydeepchaudharyqa.orangehrm.tests.pim;

import io.github.jaydeepchaudharyqa.orangehrm.base.BaseTest;
import io.github.jaydeepchaudharyqa.orangehrm.dataproviders.TestDataProviders;
import io.github.jaydeepchaudharyqa.orangehrm.models.Employee;
import io.github.jaydeepchaudharyqa.orangehrm.pages.pim.AddEmployeePage;
import io.github.jaydeepchaudharyqa.orangehrm.pages.pim.EmployeeListPage;
import io.github.jaydeepchaudharyqa.orangehrm.pages.pim.PersonalDetailsPage;
import io.github.jaydeepchaudharyqa.orangehrm.utils.TestDataGenerator;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EmployeeManagementTests extends BaseTest {

    @Test(groups = {"smoke", "regression"},
            dataProvider = "employeeTemplates", dataProviderClass = TestDataProviders.class,
            description = "End to end: add an employee, find them in PIM, then delete them")
    public void addSearchAndDeleteEmployee(Employee template) {
        Employee employee = TestDataGenerator.uniqueEmployeeFrom(template);
        EmployeeListPage employeeList = loginAsAdmin().goToPim();

        step("Add a new employee: " + employee);
        AddEmployeePage addPage = employeeList.clickAdd().fillDetails(employee);
        PersonalDetailsPage profile = addPage.save();

        step("Verify the new profile shows the right name and id");
        assertEquals(profile.getDisplayedName(), employee.getFullName(), "Name in profile header");
        assertEquals(profile.getEmployeeId(), employee.getEmployeeId(), "Employee id in profile");

        step("Search for the employee by id in the Employee List");
        employeeList = profile.goToPim().searchByEmployeeId(employee.getEmployeeId());
        assertEquals(employeeList.getEmployeeIds(), List.of(employee.getEmployeeId()), "Ids in search results");
        assertEquals(employeeList.getLastNames(), List.of(employee.getLastName()), "Last names in search results");

        step("Delete the employee");
        String toast = employeeList.deleteFirstResult();
        assertTrue(toast.contains("Successfully Deleted"), "Delete confirmation toast, was: " + toast);

        step("Verify the employee can no longer be found");
        employeeList.searchByEmployeeId(employee.getEmployeeId());
        assertEquals(employeeList.getResultCount(), 0, "Results after deletion");
    }

    @Test(groups = "regression",
            description = "Saving the Add Employee form without names shows 'Required' errors")
    public void addEmployeeRequiresNames() {
        AddEmployeePage addPage = loginAsAdmin().goToPim().clickAdd();

        step("Save the form with first and last name empty");
        addPage.saveExpectingErrors();

        step("Verify both name fields show 'Required'");
        List<String> errors = addPage.getFieldErrors();
        assertEquals(errors.stream().filter("Required"::equals).count(), 2L, "Required errors, got: " + errors);
    }
}
