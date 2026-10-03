package io.github.jaydeepchaudharyqa.orangehrm.driver;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import org.openqa.selenium.WebDriver;

/**
 * Abstraction for "a browser that can create a WebDriver".
 *
 * <p>Each supported browser extends this class and implements {@link #createDriver()}. The
 * {@link DriverFactory} only talks to this abstract type, so adding a new browser never changes
 * the calling code (abstraction + polymorphism). Common behaviour, such as applying timeouts,
 * lives here once and is inherited by every browser (inheritance).
 */
public abstract class BrowserDriver {

    protected final ConfigReader config = ConfigReader.get();

    /** Builds a browser-specific driver with its options. */
    protected abstract WebDriver createDriver();

    /** Human-readable browser name for logs and reports. */
    public abstract String name();

    /** Template method: create the driver, then apply settings shared by all browsers. */
    public final WebDriver start() {
        WebDriver driver = createDriver();
        driver.manage().timeouts().pageLoadTimeout(config.pageLoadTimeout());
        // Implicit wait stays at 0: all synchronisation is done with explicit waits.
        if (!config.headless()) {
            driver.manage().window().maximize();
        }
        return driver;
    }

    protected boolean isHeadless() {
        return config.headless();
    }
}
