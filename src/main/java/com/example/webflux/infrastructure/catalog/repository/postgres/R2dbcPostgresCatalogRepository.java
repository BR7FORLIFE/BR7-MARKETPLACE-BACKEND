package com.example.webflux.infrastructure.catalog.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.catalog.persistence.CatalogEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresCatalogRepository extends ReactiveCrudRepository<CatalogEntity, UUID> {
    Mono<Boolean> existsBySlug(String slug);

    Mono<CatalogEntity> findByCode(String code);
}
