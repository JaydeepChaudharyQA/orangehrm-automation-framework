package io.github.jaydeepchaudharyqa.orangehrm.config;

import io.github.jaydeepchaudharyqa.orangehrm.constants.FrameworkConstants;
import io.github.jaydeepchaudharyqa.orangehrm.enums.BrowserType;
import io.github.jaydeepchaudharyqa.orangehrm.exceptions.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Reads framework settings from {@code config/config.properties}.
 *
 * <p>Any key can be overridden without editing the file, in this order of priority:
 * <ol>
 *   <li>JVM system property, e.g. {@code -Dbrowser=firefox}</li>
 *   <li>Environment variable, e.g. {@code BROWSER=firefox} or {@code ADMIN_PASSWORD=...}</li>
 *   <li>The value in config.properties</li>
 * </ol>
 *
 * <p>Implemented as a lazily initialised singleton; the properties object is private and only
 * exposed through typed getters (encapsulation).
 */
public final class ConfigReader {

    private static volatile ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(FrameworkConstants.CONFIG_FILE)) {
            if (in == null) {
                throw new ConfigurationException("Config file not found on classpath: " + FrameworkConstants.CONFIG_FILE);
            }
            properties.load(in);
        } catch (IOException e) {
            throw new ConfigurationException("Could not read " + FrameworkConstants.CONFIG_FILE, e);
        }
    }

    public static ConfigReader get() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    /** Returns the value for {@code key}, applying system-property and environment overrides. */
    public String getString(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (isBlank(value)) {
            value = properties.getProperty(key);
        }
        if (isBlank(value)) {
            throw new ConfigurationException("Missing required configuration key: '" + key + "'");
        }
        return value.trim();
    }

    public int getInt(String key) {
        String value = getString(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Key '" + key + "' must be a number but was '" + value + "'", e);
        }
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getString(key));
    }

    // ---- Typed shortcuts for the settings the framework uses most ----

    public String baseUrl() {
        return getString("base.url");
    }

    public BrowserType browser() {
        return BrowserType.from(getString("browser"));
    }

    public boolean headless() {
        return getBoolean("headless");
    }

    public Duration explicitWait() {
        return Duration.ofSeconds(getInt("explicit.wait.seconds"));
    }

    public Duration pageLoadTimeout() {
        return Duration.ofSeconds(getInt("page.load.timeout.seconds"));
    }

    public int maxRetryCount() {
        return getInt("retry.max.count");
    }

    public String adminUsername() {
        return getString("admin.username");
    }

    public String adminPassword() {
        return getString("admin.password");
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
