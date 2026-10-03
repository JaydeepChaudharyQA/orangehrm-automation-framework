package io.github.jaydeepchaudharyqa.orangehrm.dataproviders;

import io.github.jaydeepchaudharyqa.orangehrm.models.Employee;
import io.github.jaydeepchaudharyqa.orangehrm.models.LoginData;
import io.github.jaydeepchaudharyqa.orangehrm.models.ModuleData;
import io.github.jaydeepchaudharyqa.orangehrm.utils.JsonDataReader;
import org.testng.annotations.DataProvider;

import java.util.List;

/** TestNG data providers. Data lives in JSON files so cases can be added without touching code. */
public final class TestDataProviders {

    private TestDataProviders() {
    }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return toRows(JsonDataReader.readList("invalid-logins.json", LoginData.class));
    }

    @DataProvider(name = "modules")
    public static Object[][] modules() {
        return toRows(JsonDataReader.readList("modules.json", ModuleData.class));
    }

    /** Employee templates from JSON; tests make each one unique with TestDataGenerator. */
    @DataProvider(name = "employeeTemplates")
    public static Object[][] employeeTemplates() {
        return toRows(JsonDataReader.readList("employees.json", Employee.class));
    }

    private static Object[][] toRows(List<?> items) {
        return items.stream().map(item -> new Object[]{item}).toArray(Object[][]::new);
    }
}
