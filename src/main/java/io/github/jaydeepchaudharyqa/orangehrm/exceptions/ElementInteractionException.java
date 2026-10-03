package io.github.jaydeepchaudharyqa.orangehrm.exceptions;

/** Thrown when an element cannot be found, waited for or interacted with. */
public class ElementInteractionException extends FrameworkException {

    public ElementInteractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
