package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class AuthStatusEmailVerifiedException extends ApplicationException {

    public AuthStatusEmailVerifiedException() {
        super("Email is not verified");
    }
}
