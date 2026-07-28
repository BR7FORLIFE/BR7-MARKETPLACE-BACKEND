package com.example.webflux.infrastructure.catalog.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "catalog_items")
public class CatalogProductItemEntity implements Persistable<UUID> {
    @Id
    @Column("catalog_id")
    private UUID id;

    @Transient
    private boolean isNew = true;

    @Column("product_id")
    private UUID productId;

    @Column("custom_label")
    private String customLabel;

    @Column("create_at")
    private Instant createAt;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
