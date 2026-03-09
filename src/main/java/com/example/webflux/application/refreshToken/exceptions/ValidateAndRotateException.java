package com.example.webflux.application.refreshToken.exceptions;

import com.example.webflux.application.zGlobalApplicationExceptions.ApplicationException;

public class ValidateAndRotateException extends ApplicationException {

    public ValidateAndRotateException() {
        super("Refresh token non-existent!");
    }
}
