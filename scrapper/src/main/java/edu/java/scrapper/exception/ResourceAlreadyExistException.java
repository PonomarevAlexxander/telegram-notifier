package edu.java.scrapper.exception;

import edu.java.resilience.error.ApplicationException;

public class ResourceAlreadyExistException extends ApplicationException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
