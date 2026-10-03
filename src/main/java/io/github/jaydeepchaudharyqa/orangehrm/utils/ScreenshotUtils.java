package io.github.jaydeepchaudharyqa.orangehrm.utils;

import io.github.jaydeepchaudharyqa.orangehrm.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/** Captures screenshots for failed tests: saved as PNG files and returned as Base64 for the report. */
public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);
    private static final DateTimeFormatter STAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    /** Saves a PNG under reports/screenshots and returns its path, or empty if capture failed. */
    public static Optional<Path> saveToFile(WebDriver driver, String testName) {
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.createDirectories(FrameworkConstants.SCREENSHOTS_DIR);
            String fileName = testName.replaceAll("[^A-Za-z0-9_-]", "_") + "_" + LocalDateTime.now().format(STAMP) + ".png";
            Path file = FrameworkConstants.SCREENSHOTS_DIR.resolve(fileName);
            Files.write(file, png);
            LOG.info("Screenshot saved: {}", file);
            return Optional.of(file);
        } catch (IOException | RuntimeException e) {
            LOG.warn("Could not save screenshot for {}: {}", testName, e.getMessage());
            return Optional.empty();
        }
    }

    /** Returns the current screen as Base64, so it can be embedded directly in the HTML report. */
    public static Optional<String> asBase64(WebDriver driver) {
        try {
            return Optional.of(((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64));
        } catch (RuntimeException e) {
            LOG.warn("Could not capture Base64 screenshot: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
