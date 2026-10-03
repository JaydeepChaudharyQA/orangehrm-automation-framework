package io.github.jaydeepchaudharyqa.orangehrm.pages;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** The OrangeHRM login screen. */
public class LoginPage extends BasePage {

    private static final String PATH = "/auth/login";

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_ALERT = By.cssSelector(".oxd-alert-content-text");
    private static final By FIELD_ERRORS = By.cssSelector(".oxd-input-field-error-message");

    public LoginPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    /** Opens the login page directly from the configured base URL. */
    public static LoginPage open(WebDriver driver) {
        driver.get(ConfigReader.get().baseUrl() + PATH);
        return new LoginPage(driver);
    }

    @Override
    protected String urlFragment() {
        return PATH;
    }

    @Override
    protected By pageIdentifier() {
        return USERNAME;
    }

    public LoginPage enterUsername(String username) {
        actions.type(USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        actions.typeSecret(PASSWORD, password);
        return this;
    }

    public void clickLogin() {
        actions.click(LOGIN_BUTTON);
    }

    /** Logs in with valid credentials and returns the dashboard. */
    public DashboardPage loginAs(String username, String password) {
        log.info("Logging in as '{}'", username);
        enterUsername(username).enterPassword(password).clickLogin();
        return new DashboardPage(driver);
    }

    /** Submits credentials that are expected to fail, staying on the login page. */
    public LoginPage attemptLogin(String username, String password) {
        log.info("Attempting login as '{}' (expected to fail)", username);
        if (!username.isEmpty()) {
            enterUsername(username);
        }
        if (!password.isEmpty()) {
            enterPassword(password);
        }
        clickLogin();
        return this;
    }

    public String getErrorMessage() {
        return actions.getText(ERROR_ALERT);
    }

    /** Messages shown under individual fields, e.g. "Required". */
    public List<String> getFieldErrors() {
        return actions.getTexts(FIELD_ERRORS);
    }
}
