package com.example.webflux.domain.emailVerificationToken.ports;

import java.util.UUID;

import com.example.webflux.domain.emailVerificationToken.models.EmailVerificationTokenModel;

import reactor.core.publisher.Mono;

public interface EmailVerificationTokenPort {
    Mono<EmailVerificationTokenModel> findById(UUID id);

    Mono<EmailVerificationTokenModel> findByUserId(UUID userId);

    Mono<EmailVerificationTokenModel> findByTokenHash(String rawToken);

    Mono<EmailVerificationTokenModel> save(EmailVerificationTokenModel model);
}
