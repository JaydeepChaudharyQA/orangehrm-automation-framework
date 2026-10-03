package io.github.jaydeepchaudharyqa.orangehrm.utils;

import io.github.jaydeepchaudharyqa.orangehrm.exceptions.ElementInteractionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Supplier;

/**
 * Reusable, synchronised element interactions. Every action waits for the right element state,
 * retries once on a stale element (common in OrangeHRM's re-rendering Vue UI), and logs what it did.
 */
public class ElementActions {

    private static final Logger LOG = LogManager.getLogger(ElementActions.class);
    private static final int STALE_RETRIES = 2;

    private final WebDriver driver;
    private final WaitUtils waits;

    public ElementActions(WebDriver driver, WaitUtils waits) {
        this.driver = driver;
        this.waits = waits;
    }

    public void click(By locator) {
        retryOnStale(() -> {
            WebElement element = waits.clickable(locator);
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                // A toast or overlay is in the way: scroll the element into view and use a JS click.
                LOG.debug("Click intercepted on {}, retrying with JavaScript", locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", element);
            }
            return null;
        }, "click", locator);
        LOG.debug("Clicked {}", locator);
    }

    /** Clears the field (in a way Vue registers) and types the text. */
    public void type(By locator, String text) {
        enterText(locator, text);
        LOG.debug("Typed '{}' into {}", text, locator);
    }

    /** Same as {@link #type} but never writes the value to the logs. */
    public void typeSecret(By locator, String secret) {
        enterText(locator, secret);
        LOG.debug("Typed ****** into {}", locator);
    }

    private void enterText(By locator, String text) {
        retryOnStale(() -> {
            WebElement element = waits.visible(locator);
            // element.clear() is not always picked up by Vue, so select-all + delete instead.
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            element.sendKeys(text);
            return null;
        }, "type into", locator);
    }

    public String getText(By locator) {
        return retryOnStale(() -> waits.visible(locator).getText().trim(), "read text of", locator);
    }

    public String getValue(By locator) {
        return retryOnStale(() -> waits.visible(locator).getDomProperty("value"), "read value of", locator);
    }

    public List<String> getTexts(By locator) {
        return retryOnStale(() -> waits.allVisible(locator).stream().map(e -> e.getText().trim()).toList(),
                "read texts of", locator);
    }

    public boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    public int count(By locator) {
        return driver.findElements(locator).size();
    }

    private <T> T retryOnStale(Supplier<T> action, String verb, By locator) {
        StaleElementReferenceException last = null;
        for (int attempt = 0; attempt <= STALE_RETRIES; attempt++) {
            try {
                return action.get();
            } catch (StaleElementReferenceException e) {
                last = e;
                LOG.debug("Stale element on attempt {} to {} {}", attempt + 1, verb, locator);
            }
        }
        throw new ElementInteractionException("Could not " + verb + " " + locator + " (element kept going stale)", last);
    }
}
