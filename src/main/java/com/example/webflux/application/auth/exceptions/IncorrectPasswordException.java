package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class IncorrectPasswordException extends ApplicationException {
    
    public IncorrectPasswordException(){
        super("The password is not correct!");
    }
}
