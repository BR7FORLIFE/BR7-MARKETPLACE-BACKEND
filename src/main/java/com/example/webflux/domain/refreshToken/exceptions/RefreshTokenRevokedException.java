package com.example.webflux.domain.refreshToken.exceptions;

import com.example.webflux.domain.zGlobalDomainExceptions.DomainException;

public class RefreshTokenRevokedException extends DomainException {
    public RefreshTokenRevokedException() {
        super("Refresh token has revoked!");
    }
}
