package com.example.webflux.infrastructure.refreshToken.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.refreshToken.models.RefreshTokenModel;
import com.example.webflux.domain.refreshToken.ports.RefreshTokenDomainRepositoryPort;
import com.example.webflux.infrastructure.refreshToken.mapper.RefreshTokenMapper;
import com.example.webflux.infrastructure.refreshToken.persistence.RefreshTokenEntity;
import com.example.webflux.infrastructure.refreshToken.repository.postgres.R2dbcPostgresRefreshTokenRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcRefreshTokenRepositoryAdapter implements RefreshTokenDomainRepositoryPort {

    private final R2dbcPostgresRefreshTokenRepository refreshTokenRepository;

    public R2dbcRefreshTokenRepositoryAdapter(R2dbcPostgresRefreshTokenRepository r2dbcPostgresRefreshTokenRepository) {
        this.refreshTokenRepository = r2dbcPostgresRefreshTokenRepository;
    }

    @Override
    public Mono<RefreshTokenModel> findById(UUID id) {
        return this.refreshTokenRepository
                .findById(id)
                .map(RefreshTokenMapper::toDomain);
    }

    @Override
    public Mono<RefreshTokenModel> findByTokenHash(String tokenHash) {
        return this.refreshTokenRepository
                .findByTokenHash(tokenHash)
                .map(RefreshTokenMapper::toDomain);
    }

    @Override
    public Flux<RefreshTokenModel> findByUserIdAndNotRevoked(UUID userId) {
        return this.refreshTokenRepository
                .findByUserIdAndRevokedIsFalse(userId)
                .map(RefreshTokenMapper::toDomain);
    }

    @Override
    public Mono<RefreshTokenModel> save(RefreshTokenModel tokenModel) {
        return this.refreshTokenRepository.existsById(tokenModel.getId())
                .flatMap(exists -> {
                    RefreshTokenEntity entity = RefreshTokenMapper.toEntity(tokenModel);

                    if (exists) {
                        entity.markNotNew();
                    }

                    return this.refreshTokenRepository.save(entity)
                            .map(RefreshTokenMapper::toDomain);
                });
    }

    @Override
    public Mono<Void> revokeAllByUserId(UUID userId) {
        return this.refreshTokenRepository.revokeAllByUserId(userId);
    }
}
