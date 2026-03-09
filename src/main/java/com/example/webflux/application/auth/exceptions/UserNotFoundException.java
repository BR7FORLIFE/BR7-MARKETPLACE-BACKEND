package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super("User not found!");
    }
}
