package com.example.webflux.domain.catalogs.models;

import java.util.UUID;

public final class CatalogModelDomain {
    private final UUID catalogId;
    private final UUID userId;
    private final String code;
    private final String slug;
    private final String name;
    private final String type;
    private final CatalogStatusEnum status;
    private final Boolean visible;

    private CatalogModelDomain(UUID catalogId, UUID userId, String code, String slug, String name, String type,
            CatalogStatusEnum status,
            Boolean visible) {
        this.catalogId = catalogId;
        this.userId = userId;
        this.code = code;
        this.slug = slug;
        this.name = name;
        this.type = type;
        this.status = status;
        this.visible = visible;
    }

    public static CatalogModelDomain createNew(UUID catalogId, UUID userId, String code, String slug, String name,
            String type, CatalogStatusEnum status, Boolean visible) {
        return new CatalogModelDomain(catalogId, userId, code, slug, name, type, status, visible);
    }

    public static CatalogModelDomain createDraft(UUID userId, String code, String slug, String name, String type,
            CatalogStatusEnum status,
            Boolean visible) {
        return new CatalogModelDomain(UUID.randomUUID(), userId, code, slug, name, type, status, visible);
    }

    public UUID getCatalogId() {
        return catalogId;
    }

    public UUID getUserId() {
        return userId;
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

    public CatalogStatusEnum getStatus() {
        return status;
    }

    public Boolean getVisible() {
        return visible;
    }

}
