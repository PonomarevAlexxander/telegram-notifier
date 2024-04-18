package edu.java.scrapper.exception;

import edu.java.resilience.error.ApplicationException;

public class LinkIsNotSupportedException extends ApplicationException {
    public LinkIsNotSupportedException(String message) {
        super(message);
    }

    public LinkIsNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
