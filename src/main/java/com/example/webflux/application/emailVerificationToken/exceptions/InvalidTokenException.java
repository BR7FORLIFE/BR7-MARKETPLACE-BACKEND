package com.example.webflux.application.emailVerificationToken.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class InvalidTokenException extends ApplicationException {

    public InvalidTokenException() {
        super("Invalid or unfound email verification token!");
    }
}
