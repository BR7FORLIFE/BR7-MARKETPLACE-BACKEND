package com.example.webflux.application.zGlobalApplicationExceptions;

public abstract class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
