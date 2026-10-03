package io.github.jaydeepchaudharyqa.orangehrm.pages.components;

import io.github.jaydeepchaudharyqa.orangehrm.utils.ElementActions;
import io.github.jaydeepchaudharyqa.orangehrm.utils.WaitUtils;
import org.openqa.selenium.WebDriver;

/**
 * Parent of reusable UI parts that appear on many pages (top bar, side menu).
 * Pages <i>have</i> components (composition), so the same component code is reused everywhere.
 */
public abstract class BaseComponent {

    protected final WebDriver driver;
    protected final WaitUtils waits;
    protected final ElementActions actions;

    protected BaseComponent(WebDriver driver) {
        this.driver = driver;
        this.waits = new WaitUtils(driver);
        this.actions = new ElementActions(driver, waits);
    }
}
