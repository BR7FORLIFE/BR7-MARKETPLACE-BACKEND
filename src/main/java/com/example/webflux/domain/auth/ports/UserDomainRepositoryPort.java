package com.example.webflux.domain.auth.ports;

import java.util.UUID;

import com.example.webflux.domain.auth.models.UserModelDomain;

import reactor.core.publisher.Mono;

public interface UserDomainRepositoryPort {
    Mono<UserModelDomain> findByUserId(UUID id);

    Mono<UserModelDomain> findByUsername(String username);

    Mono<UserModelDomain> findByEmail(String email);

    Mono<UserModelDomain> save(UserModelDomain userModelDomain);
}
