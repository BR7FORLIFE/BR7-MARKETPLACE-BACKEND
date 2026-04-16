package com.example.webflux.application.catalog.ports;

import java.util.UUID;

import com.example.webflux.domain.catalogs.models.CatalogModelDomain;

import reactor.core.publisher.Mono;

public interface CatalogPort {
    Mono<CatalogModelDomain> save(CatalogModelDomain catalogModelDomain);

    Mono<Boolean> existBySlug(String slug);

    Mono<CatalogModelDomain> findByCatalogId(UUID catalogId);

    Mono<CatalogModelDomain> findByCode(String code);
}
