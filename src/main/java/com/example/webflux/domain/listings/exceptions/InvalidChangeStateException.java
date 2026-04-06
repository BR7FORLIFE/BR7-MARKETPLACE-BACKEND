package com.example.webflux.domain.listings.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class InvalidChangeStateException extends ApplicationException {

    public InvalidChangeStateException() {
        super("Invalid in change the listing status!");
    }
}
