package com.example.webflux.application.products.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class RegisterProductException extends ApplicationException {

    public RegisterProductException() {
        super("The current product already exists!");
    }
}
