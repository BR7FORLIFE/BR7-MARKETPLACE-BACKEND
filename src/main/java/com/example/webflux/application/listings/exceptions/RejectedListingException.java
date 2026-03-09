package com.example.webflux.application.listings.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class RejectedListingException extends ApplicationException {

    public RejectedListingException() {
        super("");
    }
}
