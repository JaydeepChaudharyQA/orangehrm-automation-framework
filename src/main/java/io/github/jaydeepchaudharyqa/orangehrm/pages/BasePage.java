package io.github.jaydeepchaudharyqa.orangehrm.pages;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import io.github.jaydeepchaudharyqa.orangehrm.utils.ElementActions;
import io.github.jaydeepchaudharyqa.orangehrm.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Parent of every page object.
 *
 * <p><b>Abstraction:</b> each page only declares <i>what</i> identifies it ({@link #urlFragment()},
 * {@link #pageIdentifier()}); <i>how</i> to wait for it is implemented once here.
 * <b>Inheritance:</b> all pages get the shared driver, waits, actions and logger.
 * <b>Encapsulation:</b> locators stay private inside each page; tests only call
 * business-level methods such as {@code loginAs(...)} or {@code searchById(...)}.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils waits;
    protected final ElementActions actions;
    protected final Logger log = LogManager.getLogger(getClass());

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new WaitUtils(driver);
        this.actions = new ElementActions(driver, waits);
    }

    /** Part of the URL that is unique to this page, e.g. "/pim/viewEmployeeList". */
    protected abstract String urlFragment();

    /** An element that is only visible once this page has fully rendered. */
    protected abstract By pageIdentifier();

    /** Blocks until the page is on screen; page constructors call this so tests never act too early. */
    protected final void waitForPageToLoad() {
        waits.urlContains(urlFragment());
        waits.forPageReady();
        waits.visible(pageIdentifier());
        log.info("{} loaded", getClass().getSimpleName());
    }

    /** True if this page is currently displayed, without failing the test. */
    public boolean isLoaded() {
        return driver.getCurrentUrl().contains(urlFragment())
                && waits.isVisibleWithin(pageIdentifier(), ConfigReader.get().explicitWait());
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
