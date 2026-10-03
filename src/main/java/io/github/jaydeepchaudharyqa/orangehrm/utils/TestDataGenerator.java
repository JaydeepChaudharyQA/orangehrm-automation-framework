package io.github.jaydeepchaudharyqa.orangehrm.utils;

import io.github.jaydeepchaudharyqa.orangehrm.models.Employee;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates unique values so tests never collide with each other or with other users of the
 * shared public demo site.
 */
public final class TestDataGenerator {

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private TestDataGenerator() {
    }

    /** A numeric employee id of 8 digits (OrangeHRM allows up to 10 characters). */
    public static String uniqueEmployeeId() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(10_000_000L, 99_999_999L));
    }

    /**
     * Copies an employee template and gives it a unique id and last name. Call this inside the test
     * (not in the data provider) so that a retried test also gets fresh, non-clashing data.
     */
    public static Employee uniqueEmployeeFrom(Employee template) {
        return template.toBuilder()
                .lastName(template.getLastName() + randomName(5))
                .employeeId(uniqueEmployeeId())
                .build();
    }

    /** A capitalised random word, e.g. "Qzkfa", useful for unique last names. */
    public static String randomName(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(LETTERS.charAt(ThreadLocalRandom.current().nextInt(LETTERS.length())));
        }
        return Character.toUpperCase(sb.charAt(0)) + sb.substring(1);
    }
}
