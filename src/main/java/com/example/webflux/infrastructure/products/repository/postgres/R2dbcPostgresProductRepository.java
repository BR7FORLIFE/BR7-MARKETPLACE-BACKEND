package com.example.webflux.infrastructure.products.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.products.persistence.ProductEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresProductRepository extends ReactiveCrudRepository<ProductEntity, UUID> {
    Mono<ProductEntity> findBySku(String sku);

    Mono<Boolean> existsById(UUID id);

    Mono<Boolean> existsBySku(String sku);
}
