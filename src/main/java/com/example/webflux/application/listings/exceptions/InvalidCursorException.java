package com.example.webflux.application.listings.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class InvalidCursorException extends ApplicationException {

    public InvalidCursorException() {
        super("Invalid parser cursor!");
    }
}
