package com.example.webflux.application.catalog.ports;

import com.example.webflux.domain.catalogs.models.CatalogModelDomain;

import reactor.core.publisher.Mono;

public interface CatalogPort {
    Mono<CatalogModelDomain> save(CatalogModelDomain catalogModelDomain);
}
