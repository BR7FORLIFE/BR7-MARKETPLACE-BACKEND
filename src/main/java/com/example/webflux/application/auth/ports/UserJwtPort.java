package com.example.webflux.application.auth.ports;

import com.example.webflux.application.auth.model.AuthenticatedUser;

import reactor.core.publisher.Mono;

public interface UserJwtPort {
    Mono<String> generateAccessToken(AuthenticatedUser user);
}
