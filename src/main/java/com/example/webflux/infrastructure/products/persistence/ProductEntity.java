package com.example.webflux.infrastructure.products.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "products")
@Data
public class ProductEntity implements Persistable<UUID> {
    @Id
    @Column("product_id")
    private UUID id;

    private Boolean isNew = true;

    @Column("sku")
    private String sku;

    @Column("user_id")
    private UUID userId;

    @Column("name_product")
    private String name;

    @Column("short_description")
    private String shortDescription;

    @Column("long_description")
    private String longDescription;

    @Column("model")
    private String model;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
