package com.example.webflux.domain.emailVerificationToken.exceptions;

import com.example.webflux.domain.zGlobalDomainExceptions.DomainException;

public class EmailExpiredException extends DomainException {

    public EmailExpiredException() {
        super("");
    }
}
