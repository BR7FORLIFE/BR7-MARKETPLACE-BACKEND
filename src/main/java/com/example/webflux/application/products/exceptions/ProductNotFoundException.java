package com.example.webflux.application.products.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException() {
        super("Product not found!");
    }
}
