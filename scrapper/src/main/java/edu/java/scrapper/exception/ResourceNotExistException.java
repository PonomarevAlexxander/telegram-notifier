package edu.java.scrapper.exception;

import edu.java.resilience.error.ApplicationException;

public class ResourceNotExistException extends ApplicationException {
    public ResourceNotExistException(String message) {
        super(message);
    }

    public ResourceNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
