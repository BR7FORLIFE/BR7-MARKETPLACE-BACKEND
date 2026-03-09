package com.example.webflux.application.Authorization.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class AssigmentRolUserException extends ApplicationException {

    public AssigmentRolUserException() {
        super("The user already has the role!, please intent assigment another role");
    }
}
