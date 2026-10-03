package io.github.jaydeepchaudharyqa.orangehrm.reports;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

/**
 * Writes one test step to all three outputs at once: the log file, the Extent HTML report
 * and TestNG's own report (Reporter output shown in emailable-report.html).
 */
public final class StepLogger {

    private static final Logger LOG = LogManager.getLogger(StepLogger.class);

    private StepLogger() {
    }

    public static void step(String message) {
        LOG.info("STEP: {}", message);
        Reporter.log("STEP: " + message);
        ExtentTest test = ExtentTestManager.get();
        if (test != null) {
            test.info(message);
        }
    }
}
