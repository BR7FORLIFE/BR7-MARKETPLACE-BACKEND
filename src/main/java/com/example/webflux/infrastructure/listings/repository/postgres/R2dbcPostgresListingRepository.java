package com.example.webflux.infrastructure.listings.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.listings.persistence.ListingEntity;

import reactor.core.publisher.Mono;

public interface R2dbcPostgresListingRepository extends ReactiveCrudRepository<ListingEntity, UUID> {
    Mono<Boolean> existsById(UUID id);
}
