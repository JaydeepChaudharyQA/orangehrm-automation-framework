package io.github.jaydeepchaudharyqa.orangehrm.utils;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import io.github.jaydeepchaudharyqa.orangehrm.exceptions.ElementInteractionException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

/**
 * All synchronisation in one place. The framework never uses Thread.sleep or implicit waits;
 * every wait here is an explicit, condition-based wait with a clear failure message.
 */
public class WaitUtils {

    /** OrangeHRM shows these spinners while its Vue front end loads data. */
    private static final By LOADERS = By.cssSelector(".oxd-loading-spinner, .oxd-form-loader, .oxd-table-loader");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        this(driver, ConfigReader.get().explicitWait());
    }

    public WaitUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    public WebElement visible(By locator) {
        return until(ExpectedConditions.visibilityOfElementLocated(locator), "visible", locator);
    }

    public WebElement clickable(By locator) {
        return until(ExpectedConditions.elementToBeClickable(locator), "clickable", locator);
    }

    public WebElement present(By locator) {
        return until(ExpectedConditions.presenceOfElementLocated(locator), "present", locator);
    }

    public List<WebElement> allVisible(By locator) {
        return until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator), "visible (all)", locator);
    }

    public boolean invisible(By locator) {
        return until(ExpectedConditions.invisibilityOfElementLocated(locator), "invisible", locator);
    }

    public void urlContains(String fragment) {
        try {
            wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            throw new ElementInteractionException("URL did not contain '" + fragment + "'. Current URL: "
                    + driver.getCurrentUrl(), e);
        }
    }

    public void textPresent(By locator, String text) {
        until(ExpectedConditions.textToBePresentInElementLocated(locator, text), "showing text '" + text + "'", locator);
    }

    /** Waits until an input has a non-empty value (OrangeHRM fills some fields asynchronously). */
    public String valueNotEmpty(By locator) {
        try {
            return wait.until(d -> {
                String value = d.findElement(locator).getDomProperty("value");
                return value == null || value.isBlank() ? null : value;
            });
        } catch (TimeoutException e) {
            throw new ElementInteractionException("Input never received a value: " + locator, e);
        }
    }

    /** Waits until the element shows non-blank text and returns it. */
    public String textNotEmpty(By locator) {
        try {
            return wait.until(d -> {
                String text = d.findElement(locator).getText();
                return text == null || text.isBlank() ? null : text.trim();
            });
        } catch (TimeoutException e) {
            throw new ElementInteractionException("Element never showed any text: " + locator, e);
        }
    }

    /** Waits for the document to finish loading and for OrangeHRM's loading spinners to disappear. */
    public void forPageReady() {
        try {
            wait.until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADERS));
        } catch (TimeoutException e) {
            throw new ElementInteractionException("Page did not finish loading: " + driver.getCurrentUrl(), e);
        }
    }

    /**
     * Waits for a custom condition. Stale elements are ignored and retried automatically,
     * so the condition can safely read elements that are being re-rendered.
     */
    public <T> T until(Function<WebDriver, T> condition, String description) {
        try {
            return wait.until(condition::apply);
        } catch (TimeoutException e) {
            throw new ElementInteractionException("Timed out waiting for: " + description, e);
        }
    }

    /** Waits briefly for an element to be removed from the page; returns false if it never was. */
    public boolean staleWithin(WebElement element, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.stalenessOf(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Returns true if the element becomes visible within a short time, without failing the test. */
    public boolean isVisibleWithin(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private <T> T until(org.openqa.selenium.support.ui.ExpectedCondition<T> condition, String state, By locator) {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            throw new ElementInteractionException("Element was not " + state + " in time: " + locator, e);
        }
    }
}
