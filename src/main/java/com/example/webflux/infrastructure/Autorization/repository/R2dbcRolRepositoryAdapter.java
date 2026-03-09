package com.example.webflux.infrastructure.Autorization.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.Authorization.models.rols.RolModelDomain;
import com.example.webflux.domain.Authorization.ports.rols.RolPort;
import com.example.webflux.infrastructure.Autorization.mapper.RolMapper;
import com.example.webflux.infrastructure.Autorization.repository.postgres.R2dbcPostgresRolRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcRolRepositoryAdapter implements RolPort {

    private final R2dbcPostgresRolRepository rolRepository;

    public R2dbcRolRepositoryAdapter(R2dbcPostgresRolRepository repository) {
        this.rolRepository = repository;
    }

    @Override
    public Flux<RolModelDomain> findRols(UUID userId) {
        return rolRepository.findRolsByUserId(userId)
                .map(RolMapper::toDomain);
    }

    @Override
    public Mono<RolModelDomain> findByName(String role) {
        return rolRepository.findByRol(role)
                .map(RolMapper::toDomain);
    }

}
