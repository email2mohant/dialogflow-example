package org.example.definitions.sdk.exception;

public class NoResourceFoundException extends RuntimeException {
    public NoResourceFoundException(String resourceName) {
        super(String.format("No Resource found %s", resourceName));
    }
}
