package com.example.webflux.application.refreshToken.orchestrator;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.webflux.application.refreshToken.exceptions.ValidateAndRotateException;
import com.example.webflux.application.refreshToken.usecases.RefreshTokenUseCase;
import com.example.webflux.domain.refreshToken.models.RefreshTokenModel;
import com.example.webflux.domain.refreshToken.ports.RefreshTokenDomainRepositoryPort;
import com.example.webflux.domain.refreshToken.services.RefreshTokenDomainService;
import com.example.webflux.infrastructure.config.HashService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

@Service
public class RefreshTokenUseCaseImp implements RefreshTokenUseCase {

    private final HashService hashConfig;
    private final RefreshTokenDomainRepositoryPort repository; // <-- puerto del dominio de la feature RefreshToken
    private final Duration ttlDuration; // <-- representa una cantidad de tiempo ej: 5 Min, 30 Days, 90 seconds

    public RefreshTokenUseCaseImp(HashService hashConfig, RefreshTokenDomainRepositoryPort repo,
            @Value("${refresh-token.ttl}") Duration duration) {
        this.hashConfig = hashConfig;
        this.repository = repo;
        this.ttlDuration = duration;
    }

    // creamos el refreshToken
    /**
     * Nos basamos de la entropia para generar el RawToken
     */
    @Override
    public Mono<String> createRefreshToken(UUID userId) {
        return repository.revokeAllByUserId(userId)
                .then(Mono.fromCallable(() -> {
                    String raw = hashConfig.randomBase64(32); // <-- Raw que devolvemos en el mono Base64 con alta
                                                              // entropia
                    String hash = hashConfig.sha256(raw); // <-- Obligatorio hasearlo para seguridad por si roban la DB
                    Instant now = Instant.now();
                    Instant expiredAt = now.plus(ttlDuration);
                    RefreshTokenModel refreshtokenModel = RefreshTokenModel.createDraft(userId, hash,
                            expiredAt, expiredAt);
                    return Tuples.of(refreshtokenModel, raw);
                }).flatMap(tuple -> {
                    return repository.save(tuple.getT1()).thenReturn(tuple.getT2());
                }).subscribeOn(Schedulers.boundedElastic()));
    }

    // recibe el raw token, busca por el hash, valida y rota el token
    @Override
    public Mono<String> validateAndRotate(String rawToken) {
        String hash = hashConfig.sha256(rawToken);
        return repository.findByTokenHash(hash)
                .switchIfEmpty(Mono.error(new ValidateAndRotateException()))
                .flatMap(rt -> {
                    RefreshTokenDomainService.validateToken(rt, Instant.now()); // <-- el dominio valida si es correcto
                                                                                // o no el refresh token

                    RefreshTokenModel revoked = RefreshTokenDomainService.revoke(rt); // <- el dominio revoca la copia

                    return repository.save(revoked).then(this.createRefreshToken(rt.getUserId()));
                });
    }

    public Mono<Void> revoke(String rawToken) {
        String hash = hashConfig.sha256(rawToken);
        return repository.findByTokenHash(hash)
                .flatMap(rt -> repository.save(rt.revoke())).then();
    }
}
