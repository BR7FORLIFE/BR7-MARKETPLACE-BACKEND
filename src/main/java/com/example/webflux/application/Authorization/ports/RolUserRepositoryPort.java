package com.example.webflux.application.Authorization.ports;

import java.util.UUID;

import com.example.webflux.domain.Authorization.models.rols.RolsUsersDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolUserRepositoryPort {

    Mono<Void> assigmentRolByUser(RolsUsersDomain domain);

    Mono<Boolean> existsByUserIdAndRol(UUID userId, String role);

    Flux<String> obtainRolByUserId(UUID userId);
}
