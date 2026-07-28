package com.example.webflux.domain.catalogs.models;

import java.time.Instant;
import java.util.UUID;

public class CatalogProductItemDomain {
    private UUID catalogId;
    private UUID productId;
    private String customLabel;
    private Instant createAt;

    private CatalogProductItemDomain(UUID catalogId, UUID productId, String customLabel, Instant createAt) {
        this.catalogId = catalogId;
        this.productId = productId;
        this.customLabel = customLabel;
        this.createAt = createAt;
    }

    public static CatalogProductItemDomain createDraft(UUID catalogId, UUID productId, String customLabel) {
        return new CatalogProductItemDomain(catalogId, productId, customLabel, Instant.now());
    }

    public static CatalogProductItemDomain createNew(UUID catalogId, UUID productId, String customLabel, Instant createAt) {
        return new CatalogProductItemDomain(catalogId, productId, customLabel, createAt);
    }

    public UUID getCatalogId() {
        return catalogId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public Instant getCreateAt() {
        return createAt;
    }

}
