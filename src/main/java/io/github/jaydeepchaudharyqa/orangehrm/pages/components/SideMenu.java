package io.github.jaydeepchaudharyqa.orangehrm.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** The left navigation menu: open modules and filter the menu with its search box. */
public class SideMenu extends BaseComponent {

    private static final By SEARCH_BOX = By.cssSelector(".oxd-main-menu-search input");
    private static final By ITEM_NAMES = By.cssSelector(".oxd-main-menu .oxd-main-menu-item--name");
    private static final String ITEM_BY_NAME =
            "//ul[contains(@class,'oxd-main-menu')]//span[contains(@class,'oxd-main-menu-item--name') and normalize-space()='%s']/ancestor::a";

    public SideMenu(WebDriver driver) {
        super(driver);
    }

    /** Clicks a menu item by its visible name, e.g. "PIM" or "Admin". */
    public void open(String moduleName) {
        actions.click(By.xpath(String.format(ITEM_BY_NAME, moduleName)));
        waits.forPageReady();
    }

    public void search(String text) {
        actions.type(SEARCH_BOX, text);
    }

    public List<String> getVisibleItems() {
        return actions.getTexts(ITEM_NAMES);
    }
}
