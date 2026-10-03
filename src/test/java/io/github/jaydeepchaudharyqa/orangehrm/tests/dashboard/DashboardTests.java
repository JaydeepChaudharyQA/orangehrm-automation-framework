package io.github.jaydeepchaudharyqa.orangehrm.tests.dashboard;

import io.github.jaydeepchaudharyqa.orangehrm.base.BaseTest;
import io.github.jaydeepchaudharyqa.orangehrm.pages.DashboardPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static org.testng.Assert.assertFalse;

public class DashboardTests extends BaseTest {

    private static final List<String> CORE_WIDGETS = List.of("Time at Work", "My Actions", "Quick Launch");

    @Test(groups = {"smoke", "regression"},
            description = "The dashboard shows its core widgets")
    public void dashboardShowsCoreWidgets() {
        DashboardPage dashboard = loginAsAdmin();

        step("Read the widget titles");
        List<String> widgets = dashboard.getWidgetTitles();

        step("Verify core widgets are present: " + CORE_WIDGETS);
        // Soft assertions report every missing widget, not just the first one.
        SoftAssert soft = new SoftAssert();
        CORE_WIDGETS.forEach(w -> soft.assertTrue(widgets.contains(w), "Missing widget '" + w + "'. Found: " + widgets));
        soft.assertAll();
    }

    @Test(groups = "regression",
            description = "The logged-in user's name is shown in the top bar")
    public void topBarShowsLoggedInUser() {
        DashboardPage dashboard = loginAsAdmin();

        step("Verify the user menu shows a name");
        assertFalse(dashboard.topBar().getLoggedInUserName().isBlank(), "User name should be displayed");
    }
}
