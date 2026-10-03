package io.github.jaydeepchaudharyqa.orangehrm.tests.auth;

import io.github.jaydeepchaudharyqa.orangehrm.base.BaseTest;
import io.github.jaydeepchaudharyqa.orangehrm.dataproviders.TestDataProviders;
import io.github.jaydeepchaudharyqa.orangehrm.models.LoginData;
import io.github.jaydeepchaudharyqa.orangehrm.pages.DashboardPage;
import io.github.jaydeepchaudharyqa.orangehrm.pages.LoginPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTests extends BaseTest {

    @Test(groups = {"smoke", "regression"},
            description = "A valid Admin login opens the dashboard")
    public void validLoginOpensDashboard() {
        DashboardPage dashboard = loginAsAdmin();

        step("Verify the dashboard is shown");
        assertEquals(dashboard.getModuleTitle(), "Dashboard", "Module title after login");
        assertTrue(dashboard.getCurrentUrl().contains("/dashboard"), "URL should point to the dashboard");
    }

    @Test(groups = "regression",
            dataProvider = "invalidLogins", dataProviderClass = TestDataProviders.class,
            description = "Invalid or missing credentials are rejected with the right message")
    public void invalidLoginShowsError(LoginData data) {
        LoginPage loginPage = openLoginPage();

        step("Submit credentials: " + data.description());
        loginPage.attemptLogin(data.username(), data.password());

        step("Verify the error message '" + data.expectedError() + "'");
        if ("Required".equals(data.expectedError())) {
            assertTrue(loginPage.getFieldErrors().contains("Required"), "A 'Required' field error should be shown");
        } else {
            assertEquals(loginPage.getErrorMessage(), data.expectedError(), "Login error message");
        }
        assertTrue(loginPage.isLoaded(), "User should stay on the login page");
    }

    @Test(groups = {"smoke", "regression"},
            description = "Logging out returns the user to the login page")
    public void logoutReturnsToLoginPage() {
        DashboardPage dashboard = loginAsAdmin();

        step("Log out from the user menu");
        LoginPage loginPage = dashboard.logout();

        step("Verify the login page is shown again");
        assertTrue(loginPage.isLoaded(), "Login page should be displayed after logout");
    }
}
