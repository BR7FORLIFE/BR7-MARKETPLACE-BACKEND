package com.example.webflux.infrastructure.catalog.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("catalogs")
@Data
public class CatalogEntity implements Persistable<UUID> {

    @Id
    @Column("catalog_id")
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Transient
    private Boolean isNew = true;

    private String code;

    private String slug;

    @Column("name_catalog")
    private String nameCatalog;

    @Column("type_catalog")
    private String typeCatalog;

    private String status;

    private Boolean visible;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
