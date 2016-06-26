package com.lozano.service.exception;

/**
 * Created by jose on 14/05/16.
 */
public class DisabledAccountException extends Exception {
    public DisabledAccountException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DisabledAccountException(String message) {
        super(message);
    }

    public DisabledAccountException() {
        super();
    }
}
