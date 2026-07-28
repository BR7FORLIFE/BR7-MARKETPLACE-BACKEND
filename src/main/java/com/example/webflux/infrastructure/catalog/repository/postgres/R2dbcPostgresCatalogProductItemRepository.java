package com.example.webflux.infrastructure.catalog.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.catalog.persistence.CatalogProductItemEntity;

public interface R2dbcPostgresCatalogProductItemRepository
        extends ReactiveCrudRepository<CatalogProductItemEntity, UUID> {

}
