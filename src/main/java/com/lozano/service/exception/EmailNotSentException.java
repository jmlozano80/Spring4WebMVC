package com.lozano.service.exception;

/**
 * Created by jose on 14/05/16.
 */
public class EmailNotSentException extends Exception {

    public EmailNotSentException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EmailNotSentException(String message) {
        super(message);
    }

    public EmailNotSentException() {
        super();
    }
}
