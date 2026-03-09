package com.example.webflux.infrastructure.auth.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.auth.persistence.UserModelEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresUserRepository extends ReactiveCrudRepository<UserModelEntity, UUID> {
    Mono<UserModelEntity> findByUsername(String username);

    Mono<UserModelEntity> findByEmail(String email);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsById(UUID id);
}
