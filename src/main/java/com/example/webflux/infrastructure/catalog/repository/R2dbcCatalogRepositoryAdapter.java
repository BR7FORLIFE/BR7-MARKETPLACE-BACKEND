package com.example.webflux.infrastructure.catalog.repository;

import org.springframework.stereotype.Repository;

import com.example.webflux.application.catalog.ports.CatalogPort;
import com.example.webflux.domain.catalogs.models.CatalogModelDomain;
import com.example.webflux.infrastructure.catalog.repository.postgres.R2dbcPostgresCatalogRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcCatalogRepositoryAdapter implements CatalogPort {

    private final R2dbcPostgresCatalogRepository catalogRepository;

    public R2dbcCatalogRepositoryAdapter(R2dbcPostgresCatalogRepository repository) {
        this.catalogRepository = repository;
    }

    @Override
    public Mono<CatalogModelDomain> save(CatalogModelDomain catalogModelDomain) {
        return null;
    }
}
