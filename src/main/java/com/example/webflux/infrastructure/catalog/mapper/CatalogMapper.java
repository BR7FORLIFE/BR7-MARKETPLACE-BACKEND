package com.example.webflux.infrastructure.catalog.mapper;

import com.example.webflux.domain.catalogs.models.CatalogModelDomain;
import com.example.webflux.domain.catalogs.models.CatalogStatusEnum;
import com.example.webflux.infrastructure.catalog.persistence.CatalogEntity;

public class CatalogMapper {

    public static CatalogModelDomain toDomain(CatalogEntity catalogEntity) {
        return CatalogModelDomain.createNew(catalogEntity.getId(), catalogEntity.getUserId(), catalogEntity.getCode(),
                catalogEntity.getSlug(),
                catalogEntity.getNameCatalog(), catalogEntity.getTypeCatalog(),
                CatalogStatusEnum.valueOf(catalogEntity.getStatus()),
                catalogEntity.getVisible());
    }

    public static CatalogEntity toEntity(CatalogModelDomain catalogModelDomain) {
        CatalogEntity catalogEntity = new CatalogEntity();

        catalogEntity.setId(catalogModelDomain.getCatalogId());
        catalogEntity.setUserId(catalogModelDomain.getUserId());
        catalogEntity.setCode(catalogModelDomain.getCode());
        catalogEntity.setSlug(catalogModelDomain.getSlug());
        catalogEntity.setNameCatalog(catalogModelDomain.getName());
        catalogEntity.setTypeCatalog(catalogModelDomain.getType());
        catalogEntity.setStatus(catalogModelDomain.getStatus().name());
        catalogEntity.setVisible(catalogModelDomain.getVisible());

        return catalogEntity;
    }
}
