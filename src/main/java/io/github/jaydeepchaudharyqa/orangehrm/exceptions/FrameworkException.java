package io.github.jaydeepchaudharyqa.orangehrm.exceptions;

/**
 * Base unchecked exception for every framework-level failure.
 * Specific failures extend this class so callers can catch them broadly or narrowly (inheritance).
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
