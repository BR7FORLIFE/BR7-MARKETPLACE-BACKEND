package com.example.webflux.domain.zGlobalDomainExceptions;

public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
