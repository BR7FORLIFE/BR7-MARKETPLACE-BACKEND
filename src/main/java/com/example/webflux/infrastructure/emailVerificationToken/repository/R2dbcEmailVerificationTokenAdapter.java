package com.example.webflux.infrastructure.emailVerificationToken.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.emailVerificationToken.models.EmailVerificationTokenModel;
import com.example.webflux.domain.emailVerificationToken.ports.EmailVerificationTokenPort;
import com.example.webflux.infrastructure.emailVerificationToken.mapper.EmailVerificationTokenMapper;
import com.example.webflux.infrastructure.emailVerificationToken.persistence.EmailVerificationTokenEntity;
import com.example.webflux.infrastructure.emailVerificationToken.repository.postgres.R2dbcPostgresEmailVerificationTokenRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcEmailVerificationTokenAdapter implements EmailVerificationTokenPort {

    private final R2dbcPostgresEmailVerificationTokenRepository repository;

    public R2dbcEmailVerificationTokenAdapter(R2dbcPostgresEmailVerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<EmailVerificationTokenModel> findById(UUID id) {
        return repository.findById(id)
                .map(EmailVerificationTokenMapper::toDomain);
    }

    @Override
    public Mono<EmailVerificationTokenModel> findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .map(EmailVerificationTokenMapper::toDomain);
    }

    @Override
    public Mono<EmailVerificationTokenModel> findByTokenHash(String hash) {
        return repository.findByTokenHash(hash)
                .map(EmailVerificationTokenMapper::toDomain);
    }

    @Override
    public Mono<EmailVerificationTokenModel> save(EmailVerificationTokenModel model) {
        return repository.existsById(model.getId())
                .flatMap(exists -> {
                    EmailVerificationTokenEntity entity = EmailVerificationTokenMapper.toEntity(model);

                    if (exists) {
                        entity.markNotNew();
                    }

                    return repository.save(entity);
                })
                .map(EmailVerificationTokenMapper::toDomain);
    }
}
