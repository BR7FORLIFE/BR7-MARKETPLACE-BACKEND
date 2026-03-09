package com.example.webflux.infrastructure.refreshToken.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.refreshToken.persistence.RefreshTokenEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcPostgresRefreshTokenRepository extends ReactiveCrudRepository<RefreshTokenEntity, UUID> {

    @Query("UPDATE refresh_token SET revoked = true WHERE user_id = :userId AND revoked = false")
    Mono<Void> revokeAllByUserId(UUID userId);

    @Query("SELECT * FROM refresh_token WHERE token_hash = :token")
    Mono<RefreshTokenEntity> findByTokenHash(String token);

    Flux<RefreshTokenEntity> findByUserIdAndRevokedIsFalse(UUID userId);

    Mono<Boolean> existsById(UUID id);
}
