package io.github.jaydeepchaudharyqa.orangehrm.reports;

import com.aventstack.extentreports.ExtentTest;

/** Keeps the current report entry per thread, so parallel tests log to the right entry. */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static ExtentTest get() {
        return CURRENT.get();
    }

    public static void set(ExtentTest test) {
        CURRENT.set(test);
    }

    public static void remove() {
        CURRENT.remove();
    }
}
