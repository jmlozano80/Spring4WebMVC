package com.lozano.service.exception;

/**
 * Created by jose on 14/05/16.
 */
public class EmailExistsException extends Exception {

    public EmailExistsException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EmailExistsException(String message) {
        super(message);
    }

    public EmailExistsException() {
        super();
    }
}