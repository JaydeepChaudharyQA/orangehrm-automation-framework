package io.github.jaydeepchaudharyqa.orangehrm.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import io.github.jaydeepchaudharyqa.orangehrm.driver.DriverManager;
import io.github.jaydeepchaudharyqa.orangehrm.reports.ExtentManager;
import io.github.jaydeepchaudharyqa.orangehrm.reports.ExtentTestManager;
import io.github.jaydeepchaudharyqa.orangehrm.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

/**
 * Connects TestNG events to logging and reporting:
 * creates a report entry per test, records the result, and on failure captures a screenshot
 * that is embedded in the Extent report and linked from TestNG's own report.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);
    private static final String ATTR_BASE64 = "failureScreenshotBase64";
    private static final String ATTR_FILE = "failureScreenshotFile";

    @Override
    public void onStart(ISuite suite) {
        LOG.info("========== Suite '{}' started ==========", suite.getName());
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
        LOG.info("========== Suite '{}' finished. Report: reports/extent-report.html ==========", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = displayName(result);
        LOG.info(">>> START {}", name);
        ExtentTest test = ExtentManager.getInstance().createTest(name, result.getMethod().getDescription());
        test.assignCategory(result.getMethod().getGroups());
        test.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        ExtentTestManager.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("<<< PASS {} ({} ms)", displayName(result), duration(result));
        ExtentTestManager.get().pass("Test passed");
        ExtentTestManager.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = displayName(result);
        LOG.error("<<< FAIL {}: {}", name, result.getThrowable() == null ? "" : result.getThrowable().getMessage());
        ExtentTest test = ExtentTestManager.get();
        test.fail(result.getThrowable());
        attachScreenshot(result, test);
        ExtentTestManager.remove();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentTestManager.get();
        if (result.wasRetried()) {
            // This attempt will be re-run; drop it from the report so only the final attempt shows.
            LOG.warn("<<< RETRY {} after failure: {}", displayName(result),
                    result.getThrowable() == null ? "unknown" : result.getThrowable().getMessage().lines().findFirst().orElse(""));
            if (test != null) {
                ExtentManager.getInstance().removeTest(test);
            }
        } else {
            LOG.warn("<<< SKIP {}", displayName(result));
            if (test == null) {
                test = ExtentManager.getInstance().createTest(displayName(result));
            }
            test.skip(result.getThrowable() == null ? "Skipped" : result.getThrowable().getMessage());
        }
        ExtentTestManager.remove();
    }

    /**
     * Captures the failure screenshot while the browser is still open and stores it on the result.
     * Called from BaseTest's @AfterMethod (before the browser is closed) and, as a fallback, here.
     */
    public static void captureFailureScreenshot(ITestResult result) {
        if (result.getAttribute(ATTR_BASE64) != null || !DriverManager.hasDriver()) {
            return;
        }
        var driver = DriverManager.getDriver();
        ScreenshotUtils.asBase64(driver).ifPresent(b64 -> result.setAttribute(ATTR_BASE64, b64));
        ScreenshotUtils.saveToFile(driver, result.getMethod().getMethodName())
                .ifPresent(path -> result.setAttribute(ATTR_FILE, path));
    }

    private void attachScreenshot(ITestResult result, ExtentTest test) {
        captureFailureScreenshot(result);
        Optional.ofNullable((String) result.getAttribute(ATTR_BASE64)).ifPresent(base64 ->
                test.fail("Screenshot at the point of failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Failure screenshot").build()));
        // Link the PNG from TestNG's built-in report too.
        Optional.ofNullable((Path) result.getAttribute(ATTR_FILE)).ifPresent(path -> {
            Reporter.setCurrentTestResult(result);
            Reporter.log("Screenshot: <a href='" + path.toUri() + "'>" + path.getFileName() + "</a>");
            Reporter.setCurrentTestResult(null);
        });
    }

    private static String displayName(ITestResult result) {
        Object[] params = result.getParameters();
        String base = result.getMethod().getMethodName();
        return params.length == 0 ? base : base + " " + Arrays.toString(params);
    }

    private static long duration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
