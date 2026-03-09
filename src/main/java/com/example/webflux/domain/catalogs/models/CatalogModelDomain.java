package com.example.webflux.domain.catalogs.models;

import java.util.UUID;

public final class CatalogModelDomain {
    private final UUID catalogId;
    private final String code;
    private final String slug;
    private final String name;
    private final String type;
    private final String status;
    private final Boolean visible;

    private CatalogModelDomain(UUID catalogId, String code, String slug, String name, String type, String status,
            Boolean visible) {
        this.catalogId = catalogId;
        this.code = code;
        this.slug = slug;
        this.name = name;
        this.type = type;
        this.status = status;
        this.visible = visible;
    }

    public static CatalogModelDomain createCatalogModel(UUID catalogId, String code, String slug, String name,
            String type, String status, Boolean visible) {
        return new CatalogModelDomain(catalogId, code, slug, name, type, slug, visible);
    }

    public UUID getCatalogId() {
        return catalogId;
    }

    public String getCode() {
        return code;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getVisible() {
        return visible;
    }

}
