package com.example.webflux.infrastructure.catalog.mapper;

import com.example.webflux.domain.catalogs.models.CatalogProductItemDomain;
import com.example.webflux.infrastructure.catalog.persistence.CatalogProductItemEntity;

public class CatalogProductItemMapper {
    public static CatalogProductItemDomain toDomain(CatalogProductItemEntity entity) {
        return CatalogProductItemDomain.createNew(entity.getId(), entity.getProductId(), entity.getCustomLabel(),
                entity.getCreateAt());
    }

    public static CatalogProductItemEntity toEntity(CatalogProductItemDomain domain) {
        return CatalogProductItemEntity
                .builder()
                .id(domain.getCatalogId())
                .productId(domain.getProductId())
                .customLabel(domain.getCustomLabel())
                .createAt(domain.getCreateAt())
                .build();
    }
}
