package com.example.webflux.application.auth.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class JwtExpirationException extends ApplicationException {
    public JwtExpirationException() {
        super("Jwt expired!");
    }
}
