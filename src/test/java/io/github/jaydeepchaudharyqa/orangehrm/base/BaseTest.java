package io.github.jaydeepchaudharyqa.orangehrm.base;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import io.github.jaydeepchaudharyqa.orangehrm.driver.DriverFactory;
import io.github.jaydeepchaudharyqa.orangehrm.driver.DriverManager;
import io.github.jaydeepchaudharyqa.orangehrm.listeners.TestListener;
import io.github.jaydeepchaudharyqa.orangehrm.pages.DashboardPage;
import io.github.jaydeepchaudharyqa.orangehrm.pages.LoginPage;
import io.github.jaydeepchaudharyqa.orangehrm.reports.StepLogger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Parent of every test class. Gives each test a fresh browser (so tests are independent and can
 * run in parallel), closes it afterwards, and offers shared helpers such as {@link #loginAsAdmin()}.
 */
public abstract class BaseTest {

    protected final ConfigReader config = ConfigReader.get();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.setDriver(DriverFactory.createDriver(config.browser()));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Capture while the browser is still open; the listener attaches it to the reports.
            TestListener.captureFailureScreenshot(result);
        }
        DriverManager.quitDriver();
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected LoginPage openLoginPage() {
        step("Open the OrangeHRM login page");
        return LoginPage.open(driver());
    }

    protected DashboardPage loginAsAdmin() {
        LoginPage loginPage = openLoginPage();
        step("Log in as the Admin user");
        return loginPage.loginAs(config.adminUsername(), config.adminPassword());
    }

    /** Records a readable test step in the log file, the Extent report and the TestNG report. */
    protected void step(String description) {
        StepLogger.step(description);
    }
}
