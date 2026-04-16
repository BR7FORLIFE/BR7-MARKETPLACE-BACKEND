package com.example.webflux.application.catalog.command;

import java.util.UUID;

import com.example.webflux.domain.catalogs.models.CatalogStatusEnum;

public record CreateCatalogCommand(String slug, String nameCatalog, String typeCatalog,
        CatalogStatusEnum status, Boolean visible, UUID userId) {

}
