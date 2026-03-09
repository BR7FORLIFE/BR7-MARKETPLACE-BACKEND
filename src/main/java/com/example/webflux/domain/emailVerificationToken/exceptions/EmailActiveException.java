package com.example.webflux.domain.emailVerificationToken.exceptions;

import com.example.webflux.domain.zGlobalDomainExceptions.DomainException;

public class EmailActiveException extends DomainException {

    public EmailActiveException() {
        super("");
    }
}
