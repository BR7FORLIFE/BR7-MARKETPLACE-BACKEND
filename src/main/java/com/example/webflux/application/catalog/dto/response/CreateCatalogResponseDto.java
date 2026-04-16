package com.example.webflux.application.catalog.dto.response;

import com.example.webflux.domain.catalogs.models.CatalogStatusEnum;

public record CreateCatalogResponseDto(String code, String typeCatalog, CatalogStatusEnum status,
        Boolean visible, String message) {

}
