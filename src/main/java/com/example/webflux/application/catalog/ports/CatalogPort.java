package com.example.webflux.application.catalog.ports;

import java.util.List;
import java.util.UUID;

import com.example.webflux.domain.catalogs.models.CatalogModelDomain;
import com.example.webflux.domain.catalogs.models.CatalogProductItemDomain;

import reactor.core.publisher.Mono;

public interface CatalogPort {
    Mono<CatalogModelDomain> save(CatalogModelDomain catalogModelDomain);

    Mono<Boolean> existBySlug(String slug);

    Mono<CatalogModelDomain> findByCatalogId(UUID catalogId);

    Mono<CatalogModelDomain> findByCode(String code);

    // Catalog items
    Mono<List<CatalogProductItemDomain>> saveAll(List<CatalogProductItemDomain> catalogProductItemDomains);
}
