package com.example.webflux.infrastructure.Autorization.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.application.Authorization.ports.RolUserRepositoryPort;
import com.example.webflux.domain.Authorization.models.rols.RolsUsersDomain;
import com.example.webflux.infrastructure.Autorization.mapper.RolsUserMapper;
import com.example.webflux.infrastructure.Autorization.repository.postgres.R2dbcPostgresRolUserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcRolUserRepositoryAdapter implements RolUserRepositoryPort {

    private final R2dbcPostgresRolUserRepository rolUserRepository;

    public R2dbcRolUserRepositoryAdapter(R2dbcPostgresRolUserRepository rolUserRepository) {
        this.rolUserRepository = rolUserRepository;
    }

    @Override
    public Mono<Void> assigmentRolByUser(RolsUsersDomain domain) {
        return rolUserRepository.save(RolsUserMapper.toEntity(domain))
                .then();
    }

    @Override
    public Mono<Boolean> existsByUserIdAndRol(UUID userId, String role) {
        return rolUserRepository.existsByUserIdAndRol(userId, role);
    }

    @Override
    public Flux<String> obtainRolByUserId(UUID userId) {
        return rolUserRepository.obtainRolByUserId(userId);
    }
}
