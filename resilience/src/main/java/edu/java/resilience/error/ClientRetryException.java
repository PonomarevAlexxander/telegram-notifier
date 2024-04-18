package edu.java.resilience.error;

public class ClientRetryException extends ApplicationException {
    public ClientRetryException(String message) {
        super(message);
    }

    public ClientRetryException(String message, Throwable cause) {
        super(message, cause);
    }
}
