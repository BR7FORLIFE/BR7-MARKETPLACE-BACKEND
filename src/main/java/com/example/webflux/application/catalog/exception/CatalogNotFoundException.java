package com.example.webflux.application.catalog.exception;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class CatalogNotFoundException extends ApplicationException {
    public CatalogNotFoundException(){
        super("Catalog Not Found!");
    }
}
