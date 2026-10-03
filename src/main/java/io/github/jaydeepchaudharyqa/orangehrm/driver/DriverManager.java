package io.github.jaydeepchaudharyqa.orangehrm.driver;

import io.github.jaydeepchaudharyqa.orangehrm.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Holds one WebDriver per test thread, so tests can run in parallel without sharing a browser.
 */
public final class DriverManager {

    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new FrameworkException("No WebDriver for this thread. Was BaseTest.setUp() run?");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                LOG.warn("Browser did not close cleanly: {}", e.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }
}
