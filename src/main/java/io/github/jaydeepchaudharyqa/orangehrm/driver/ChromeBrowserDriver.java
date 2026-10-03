package io.github.jaydeepchaudharyqa.orangehrm.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

/** Google Chrome. Selenium Manager downloads the matching chromedriver automatically. */
public class ChromeBrowserDriver extends BrowserDriver {

    @Override
    protected WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications", "--disable-search-engine-choice-screen",
                "--window-size=1920,1080");
        // Stop Chrome's password manager pop-ups from covering the page.
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        if (isHeadless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new ChromeDriver(options);
    }

    @Override
    public String name() {
        return "Chrome";
    }
}
