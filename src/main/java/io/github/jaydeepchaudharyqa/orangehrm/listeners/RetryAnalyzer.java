package io.github.jaydeepchaudharyqa.orangehrm.listeners;

import io.github.jaydeepchaudharyqa.orangehrm.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Re-runs a failed test up to {@code retry.max.count} times. Useful against a shared public
 * demo site, where a slow response can fail an otherwise correct test.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        int max = ConfigReader.get().maxRetryCount();
        if (attempts < max) {
            attempts++;
            LOG.warn("Retrying '{}' (attempt {} of {})", result.getName(), attempts, max);
            return true;
        }
        return false;
    }
}
