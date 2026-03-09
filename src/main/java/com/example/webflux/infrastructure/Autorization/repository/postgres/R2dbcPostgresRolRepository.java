package com.example.webflux.infrastructure.Autorization.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.Autorization.persistence.rols.RolModelEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcPostgresRolRepository extends ReactiveCrudRepository<RolModelEntity, UUID> {

    @Query("""
            SELECT r.* FROM rols r
            JOIN users_rols ur ON ur.rol_id = r.rol_id
            WHERE ur.user_id = :userId
            """)
    Flux<RolModelEntity> findRolsByUserId(UUID userId);

    Mono<RolModelEntity> findByRol(String rol);
}
