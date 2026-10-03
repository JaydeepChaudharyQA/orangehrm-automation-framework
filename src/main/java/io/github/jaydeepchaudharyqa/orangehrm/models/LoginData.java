package io.github.jaydeepchaudharyqa.orangehrm.models;

/**
 * One row of login test data (see testdata/invalid-logins.json). Records are immutable,
 * so test data cannot be changed accidentally while a test runs.
 */
public record LoginData(String description, String username, String password, String expectedError) {

    @Override
    public String toString() {
        // Shown as the test name in reports; never prints the password.
        return description;
    }
}
