package io.github.jaydeepchaudharyqa.orangehrm.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

/** Microsoft Edge. Selenium Manager downloads the matching msedgedriver automatically. */
public class EdgeBrowserDriver extends BrowserDriver {

    @Override
    protected WebDriver createDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications", "--window-size=1920,1080");
        if (isHeadless()) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        return new EdgeDriver(options);
    }

    @Override
    public String name() {
        return "Edge";
    }
}
