package io.github.jaydeepchaudharyqa.orangehrm.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import io.github.jaydeepchaudharyqa.orangehrm.constants.FrameworkConstants;

/** Creates the single ExtentReports instance that writes reports/extent-report.html. */
public final class ExtentManager {

    private static ExtentReports extent;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT.toString());
            spark.config().setDocumentTitle("OrangeHRM Automation Report");
            spark.config().setReportName("OrangeHRM — Selenium + TestNG Execution Report");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setTimeStampFormat("dd MMM yyyy, HH:mm:ss");

            ConfigReader config = ConfigReader.get();
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", config.baseUrl());
            extent.setSystemInfo("Browser", config.browser().name() + (config.headless() ? " (headless)" : ""));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
