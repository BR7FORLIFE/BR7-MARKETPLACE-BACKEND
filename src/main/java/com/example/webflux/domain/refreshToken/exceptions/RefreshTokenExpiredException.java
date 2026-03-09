package com.example.webflux.domain.refreshToken.exceptions;

import com.example.webflux.domain.zGlobalDomainExceptions.DomainException;

public class RefreshTokenExpiredException extends DomainException {

    public RefreshTokenExpiredException() {
        super("Refresh token has expired!");
    }
}
