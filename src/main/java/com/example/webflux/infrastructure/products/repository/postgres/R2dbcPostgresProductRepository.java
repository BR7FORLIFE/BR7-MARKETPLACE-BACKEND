package com.example.webflux.infrastructure.products.repository.postgres;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.products.persistence.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcPostgresProductRepository extends ReactiveCrudRepository<ProductEntity, UUID> {
    Mono<ProductEntity> findBySku(String sku);

    Mono<Boolean> existsById(UUID id);

    Mono<Boolean> existsBySku(String sku);

    @Query("""
                SELECT p.* FROM products p
                JOIN users u ON p.user_id = u.user_id
                WHERE u.user_id = :userId
                ORDER BY create_at ASC
                LIMIT :limit OFFSET :offset
            """)
    Flux<ProductEntity> findAllProductsByUserIdAsc(UUID userId, int limit, int offset);

    @Query("""
                SELECT p.* FROM products p
                JOIN users u ON p.user_id = u.user_id
                WHERE u.user_id = :userId
                ORDER BY create_at DESC
                LIMIT :limit OFFSET :offset
            """)
    Flux<ProductEntity> findAllProductsByUserIdDesc(UUID userId, int limit, int offset);

    @Query("""
                SELECT * FROM products
                WHERE update_at > :updateAt
                ORDER BY update_at DESC
                LIMIT :limit OFFSET :offset
            """)
    Flux<ProductEntity> findAllWithDate(Instant updateAt, int limit, int offset);

    @Query("""
                SELECT * FROM products
                ORDER BY update_at DESC
                LIMIT :limit OFFSET :offset
            """)
    Flux<ProductEntity> findAllWithoutDate(int limit, int offset);

    // esto obtiene un solo producto donde el product id y el user id hagan match
    Mono<ProductEntity> findByIdAndUserId(UUID productId, UUID userId);
}
