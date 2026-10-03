package io.github.jaydeepchaudharyqa.orangehrm.tests.admin;

import io.github.jaydeepchaudharyqa.orangehrm.base.BaseTest;
import io.github.jaydeepchaudharyqa.orangehrm.pages.admin.SystemUsersPage;
import io.github.jaydeepchaudharyqa.orangehrm.utils.TestDataGenerator;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SystemUserTests extends BaseTest {

    @Test(groups = {"smoke", "regression"},
            description = "Searching by username finds the Admin account")
    public void searchByUsernameFindsAdmin() {
        String username = config.adminUsername();
        SystemUsersPage users = loginAsAdmin().goToAdmin();

        step("Search system users for '" + username + "'");
        users.searchByUsername(username);

        step("Verify the Admin account is listed with the Admin role");
        List<String> usernames = users.getUsernames();
        assertTrue(usernames.contains(username), "Usernames in results: " + usernames);
        assertEquals(users.getRoles().get(usernames.indexOf(username)), "Admin", "Role of " + username);
    }

    @Test(groups = "regression",
            description = "Filtering by the Admin role only returns Admin users")
    public void filterByRoleShowsOnlyThatRole() {
        SystemUsersPage users = loginAsAdmin().goToAdmin();

        step("Filter system users by role 'Admin'");
        users.filterByRole("Admin");

        step("Verify every result has the Admin role");
        List<String> roles = users.getRoles();
        assertFalse(roles.isEmpty(), "At least one Admin user should exist");
        assertTrue(roles.stream().allMatch("Admin"::equals), "Roles in results: " + roles);
    }

    @Test(groups = "regression",
            description = "Searching for a username that does not exist shows no records")
    public void searchUnknownUsernameShowsNoRecords() {
        String unknown = "no_user_" + TestDataGenerator.randomName(8).toLowerCase();
        SystemUsersPage users = loginAsAdmin().goToAdmin();

        step("Search for a username that does not exist: " + unknown);
        users.searchByUsername(unknown);

        step("Verify no records are returned");
        assertEquals(users.getResultCount(), 0, "Rows in results");
        assertEquals(users.getRecordsLabel(), "No Records Found", "Records summary");
    }
}
