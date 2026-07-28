package com.example.webflux.infrastructure.catalog.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.application.catalog.ports.CatalogPort;
import com.example.webflux.domain.catalogs.models.CatalogModelDomain;
import com.example.webflux.domain.catalogs.models.CatalogProductItemDomain;
import com.example.webflux.infrastructure.catalog.mapper.CatalogMapper;
import com.example.webflux.infrastructure.catalog.mapper.CatalogProductItemMapper;
import com.example.webflux.infrastructure.catalog.persistence.CatalogEntity;
import com.example.webflux.infrastructure.catalog.persistence.CatalogProductItemEntity;
import com.example.webflux.infrastructure.catalog.repository.postgres.R2dbcPostgresCatalogProductItemRepository;
import com.example.webflux.infrastructure.catalog.repository.postgres.R2dbcPostgresCatalogRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcCatalogRepositoryAdapter implements CatalogPort {

    private final R2dbcPostgresCatalogRepository catalogRepository;
    private final R2dbcPostgresCatalogProductItemRepository catalogProductItemRepository;

    public R2dbcCatalogRepositoryAdapter(R2dbcPostgresCatalogRepository repository,
            R2dbcPostgresCatalogProductItemRepository catalogProductItemRepository) {
        this.catalogRepository = repository;
        this.catalogProductItemRepository = catalogProductItemRepository;
    }

    @SuppressWarnings("null")
    @Override
    public Mono<CatalogModelDomain> save(CatalogModelDomain catalogModelDomain) {
        return catalogRepository.existsById(catalogModelDomain.getCatalogId())
                .flatMap(exists -> {
                    CatalogEntity catalogEntity = CatalogMapper.toEntity(catalogModelDomain);

                    if (exists) {
                        catalogEntity.markNotNew();
                    }

                    return catalogRepository.save(catalogEntity)
                            .map(CatalogMapper::toDomain);
                });
    }

    @Override
    public Mono<Boolean> existBySlug(String slug) {
        return catalogRepository.existsBySlug(slug);
    }

    @Override
    public Mono<CatalogModelDomain> findByCode(String code) {
        return catalogRepository.findByCode(code)
                .map(CatalogMapper::toDomain);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<CatalogModelDomain> findByCatalogId(UUID catalogId) {
        return catalogRepository.findById(catalogId)
                .map(CatalogMapper::toDomain);
    }

    @Override
    public Mono<List<CatalogProductItemDomain>> saveAll(List<CatalogProductItemDomain> catalogProductItemDomains) {
        List<CatalogProductItemEntity> catalogProductItemEntities = catalogProductItemDomains
                .stream()
                .map(CatalogProductItemMapper::toEntity)
                .toList();

        return catalogProductItemRepository.saveAll(catalogProductItemEntities)
                .map(CatalogProductItemMapper::toDomain)
                .collectList();
    }
}
