package com.example.webflux.domain.Authorization.ports.rols;

import java.util.UUID;

import com.example.webflux.domain.Authorization.models.rols.RolsUsersDomain;

import reactor.core.publisher.Mono;

public interface RolUserRepositoryPort {
    
    Mono<Void> assigmentRolByUser(RolsUsersDomain domain);

    Mono<Boolean> existsByUserIdAndRol(UUID userId, String role);
}
