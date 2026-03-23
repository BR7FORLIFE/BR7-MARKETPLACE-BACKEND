package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class JwtInvalidSignature extends ApplicationException {
    public JwtInvalidSignature() {
        super("Invalid Signature!");
    }
}
