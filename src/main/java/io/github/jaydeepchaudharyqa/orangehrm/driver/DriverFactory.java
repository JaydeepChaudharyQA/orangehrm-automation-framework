package io.github.jaydeepchaudharyqa.orangehrm.driver;

import io.github.jaydeepchaudharyqa.orangehrm.enums.BrowserType;
import io.github.jaydeepchaudharyqa.orangehrm.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/** Factory that turns a {@link BrowserType} into a running {@link WebDriver}. */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createDriver(BrowserType type) {
        // Polymorphism: every branch returns a BrowserDriver; start() behaves per browser.
        BrowserDriver browser = switch (type) {
            case CHROME -> new ChromeBrowserDriver();
            case FIREFOX -> new FirefoxBrowserDriver();
            case EDGE -> new EdgeBrowserDriver();
        };
        try {
            LOG.info("Starting {} (headless={})", browser.name(), browser.isHeadless());
            return browser.start();
        } catch (WebDriverException e) {
            throw new FrameworkException("Could not start " + browser.name()
                    + ". Is the browser installed on this machine?", e);
        }
    }
}
