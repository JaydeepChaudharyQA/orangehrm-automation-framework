package io.github.jaydeepchaudharyqa.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/** The landing page after login, made of dashboard widgets. */
public class DashboardPage extends AuthenticatedPage {

    private static final By WIDGET_TITLES = By.cssSelector(".orangehrm-dashboard-widget-name p");

    public DashboardPage(WebDriver driver) {
        super(driver);
        waitForPageToLoad();
    }

    @Override
    protected String urlFragment() {
        return "/dashboard/index";
    }

    @Override
    protected By pageIdentifier() {
        return WIDGET_TITLES;
    }

    public List<String> getWidgetTitles() {
        return actions.getTexts(WIDGET_TITLES);
    }
}
