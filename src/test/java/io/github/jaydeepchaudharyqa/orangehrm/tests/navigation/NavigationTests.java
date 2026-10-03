package io.github.jaydeepchaudharyqa.orangehrm.tests.navigation;

import io.github.jaydeepchaudharyqa.orangehrm.base.BaseTest;
import io.github.jaydeepchaudharyqa.orangehrm.dataproviders.TestDataProviders;
import io.github.jaydeepchaudharyqa.orangehrm.models.ModuleData;
import io.github.jaydeepchaudharyqa.orangehrm.pages.DashboardPage;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class NavigationTests extends BaseTest {

    @Test(groups = "regression",
            dataProvider = "modules", dataProviderClass = TestDataProviders.class,
            description = "Each side-menu item opens the right module")
    public void sideMenuOpensModule(ModuleData module) {
        DashboardPage dashboard = loginAsAdmin();

        step("Open '" + module.menuItem() + "' from the side menu");
        dashboard.sideMenu().open(module.menuItem());

        step("Verify the module title is '" + module.expectedTitle() + "'");
        assertEquals(dashboard.topBar().waitForModuleTitle(module.expectedTitle()), module.expectedTitle(),
                "Module title after opening " + module.menuItem());
    }

    @Test(groups = "regression",
            description = "Typing in the menu search box filters the menu items")
    public void menuSearchFiltersItems() {
        DashboardPage dashboard = loginAsAdmin();

        step("Search the side menu for 'Recr'");
        dashboard.sideMenu().search("Recr");

        step("Verify only 'Recruitment' is listed");
        assertEquals(dashboard.sideMenu().getVisibleItems(), List.of("Recruitment"), "Filtered menu items");
    }
}
