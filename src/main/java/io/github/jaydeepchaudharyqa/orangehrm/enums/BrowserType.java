package io.github.jaydeepchaudharyqa.orangehrm.enums;

import io.github.jaydeepchaudharyqa.orangehrm.exceptions.ConfigurationException;

import java.util.Arrays;

/** Browsers the framework can run on. */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    /** Parses a config value such as "chrome" or "Edge" into a {@link BrowserType}. */
    public static BrowserType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new ConfigurationException(
                        "Unsupported browser '" + value + "'. Use one of: " + Arrays.toString(values())));
    }
}
