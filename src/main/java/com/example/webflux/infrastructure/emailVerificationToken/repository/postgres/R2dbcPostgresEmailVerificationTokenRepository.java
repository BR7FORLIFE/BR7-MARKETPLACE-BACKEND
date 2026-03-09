package com.example.webflux.infrastructure.emailVerificationToken.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.emailVerificationToken.persistence.EmailVerificationTokenEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresEmailVerificationTokenRepository
        extends ReactiveCrudRepository<EmailVerificationTokenEntity, UUID> {
    Mono<EmailVerificationTokenEntity> findById(UUID id);

    Mono<EmailVerificationTokenEntity> findByUserId(UUID id);

    Mono<EmailVerificationTokenEntity> save(EmailVerificationTokenEntity entity);

    Mono<EmailVerificationTokenEntity> findByTokenHash(String hash);

    Mono<Boolean> existsById(UUID id);
}
