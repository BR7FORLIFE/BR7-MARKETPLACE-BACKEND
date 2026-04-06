package com.example.webflux.application.Authorization.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class AccessDeniedException extends ApplicationException {

    public AccessDeniedException() {
        super("Operation not allowed!");
    }
}
