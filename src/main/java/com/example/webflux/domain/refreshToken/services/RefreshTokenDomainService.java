package com.example.webflux.domain.refreshToken.services;

import java.time.Instant;

import com.example.webflux.domain.refreshToken.exceptions.RefreshTokenExpiredException;
import com.example.webflux.domain.refreshToken.exceptions.RefreshTokenRevokedException;
import com.example.webflux.domain.refreshToken.models.RefreshTokenModel;

public class RefreshTokenDomainService {

    public static void validateToken(RefreshTokenModel token, Instant now) {
        if (token.isRevoked()) {
            throw new RefreshTokenRevokedException();
        }

        if (token.isExpired(now)) {
            throw new RefreshTokenExpiredException();
        }
    }

    public static RefreshTokenModel revoke(RefreshTokenModel token) {
        return token.revoke();
    }
}
