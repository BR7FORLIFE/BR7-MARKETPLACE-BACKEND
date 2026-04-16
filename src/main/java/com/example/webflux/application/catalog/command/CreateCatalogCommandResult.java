package com.example.webflux.application.catalog.command;

import com.example.webflux.domain.catalogs.models.CatalogStatusEnum;

public record CreateCatalogCommandResult(String code, String typeCatalog, CatalogStatusEnum status,
                Boolean visible, String message) {

}
