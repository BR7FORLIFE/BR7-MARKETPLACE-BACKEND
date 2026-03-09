package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class UserAlreadyRegisterException extends ApplicationException {

    public UserAlreadyRegisterException() {
        super("user already register!");
    }

    
}
