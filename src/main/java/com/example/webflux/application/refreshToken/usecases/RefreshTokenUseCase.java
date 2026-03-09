package com.example.webflux.application.refreshToken.usecases;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface RefreshTokenUseCase {
    Mono<String> createRefreshToken(UUID userId);

    Mono<String> validateAndRotate(String rawToken);

    Mono<Void> revoke(String rawToken);
}
