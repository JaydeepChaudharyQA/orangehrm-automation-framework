package io.github.jaydeepchaudharyqa.orangehrm.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jaydeepchaudharyqa.orangehrm.constants.FrameworkConstants;
import io.github.jaydeepchaudharyqa.orangehrm.exceptions.FrameworkException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** Loads test data from JSON files under src/test/resources/testdata into typed Java objects. */
public final class JsonDataReader {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonDataReader() {
    }

    /** Reads a JSON array, e.g. {@code readList("invalid-logins.json", LoginData.class)}. */
    public static <T> List<T> readList(String fileName, Class<T> type) {
        String path = FrameworkConstants.TEST_DATA_DIR + fileName;
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new FrameworkException("Test data file not found on classpath: " + path);
            }
            return MAPPER.readValue(in, MAPPER.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException e) {
            throw new FrameworkException("Could not parse test data file " + path, e);
        }
    }
}
