package com.utilitys.api.exception;

public class UtilityException extends RuntimeException {

    public UtilityException(String message) {
        super(message);
    }

    public UtilityException(String message, Throwable cause) {
        super(message, cause);
    }
}
