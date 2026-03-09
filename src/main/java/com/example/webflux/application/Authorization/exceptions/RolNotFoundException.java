package com.example.webflux.application.Authorization.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class RolNotFoundException extends ApplicationException {

    public RolNotFoundException() {
        super("The current role doesnt not exists!");
    }
}
