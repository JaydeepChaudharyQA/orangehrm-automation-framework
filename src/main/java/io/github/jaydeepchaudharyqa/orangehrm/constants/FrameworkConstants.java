package io.github.jaydeepchaudharyqa.orangehrm.constants;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Fixed paths and names used across the framework. */
public final class FrameworkConstants {

    private FrameworkConstants() {
    }

    public static final String CONFIG_FILE = "config/config.properties";
    public static final String TEST_DATA_DIR = "testdata/";

    public static final Path REPORTS_DIR = Paths.get(System.getProperty("user.dir"), "reports");
    public static final Path SCREENSHOTS_DIR = REPORTS_DIR.resolve("screenshots");
    public static final Path EXTENT_REPORT = REPORTS_DIR.resolve("extent-report.html");

    public static final String GROUP_SMOKE = "smoke";
    public static final String GROUP_REGRESSION = "regression";
}
