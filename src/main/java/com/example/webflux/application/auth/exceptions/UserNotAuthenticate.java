package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class UserNotAuthenticate extends ApplicationException {
    public UserNotAuthenticate() {
        super("User not authenticate!");
    }
}
