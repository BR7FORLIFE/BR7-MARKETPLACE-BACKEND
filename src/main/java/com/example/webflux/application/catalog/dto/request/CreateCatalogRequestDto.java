package com.example.webflux.application.catalog.dto.request;

import com.example.webflux.domain.catalogs.models.CatalogStatusEnum;

public record CreateCatalogRequestDto(String code, String slug, String nameCatalog, String typeCatalog,
        CatalogStatusEnum status, Boolean visible) {

}
