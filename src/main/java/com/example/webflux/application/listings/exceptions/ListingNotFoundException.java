package com.example.webflux.application.listings.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class ListingNotFoundException extends ApplicationException{
    
    public ListingNotFoundException(){
        super("Listing not found!");
    }
}
