package io.github.jaydeepchaudharyqa.orangehrm.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Mozilla Firefox. Selenium Manager downloads the matching geckodriver automatically. */
public class FirefoxBrowserDriver extends BrowserDriver {

    @Override
    protected WebDriver createDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        if (isHeadless()) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    @Override
    public String name() {
        return "Firefox";
    }
}
