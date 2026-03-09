package com.example.webflux.domain.Authorization.ports.rols;

import java.util.UUID;

import com.example.webflux.domain.Authorization.models.rols.RolModelDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RolPort {
    Flux<RolModelDomain> findRols(UUID userId);

    Mono<RolModelDomain> findByName(String role);
}
