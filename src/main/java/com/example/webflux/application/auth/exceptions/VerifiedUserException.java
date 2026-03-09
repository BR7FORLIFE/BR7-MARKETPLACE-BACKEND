package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class VerifiedUserException extends ApplicationException {

    public VerifiedUserException() {
        super("");
    }
}
