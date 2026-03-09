package com.example.webflux.infrastructure.Autorization.repository.postgres;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.Autorization.persistence.rols.RolsUsersEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresRolUserRepository extends ReactiveCrudRepository<RolsUsersEntity, Integer> {

    Mono<RolsUsersEntity> save(RolsUsersEntity entity);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM users_rols ur
                JOIN rols r ON r.rol_id = ur.rol_id
                WHERE ur.user_id = :userId
                AND r.rol = :role
                        )
            """)
    Mono<Boolean> existsByUserIdAndRol(UUID userId, String role);
}
