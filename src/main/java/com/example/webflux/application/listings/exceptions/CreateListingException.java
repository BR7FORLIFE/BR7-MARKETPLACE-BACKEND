package com.example.webflux.application.listings.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class CreateListingException extends ApplicationException {

    public CreateListingException() {
        super("Error to create the listing! - The product already exists!");
    }
}
