package com.example.webflux.infrastructure.listings.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "listings")
@Data
public class ListingEntity implements Persistable<UUID> {
    @Id
    @Column("listing_id")
    private UUID id;

    @Transient
    private Boolean isNew = true;

    @Column("product_id")
    private UUID productId;

    @Column("price")
    private Double price;

    @Column("currency")
    private String currency;

    @Column("is_active")
    private Boolean isActive;

    @Column("status_review")
    private String statusReview;

    @Column("status_publication")
    private String statusPublication;

    @Column("create_at")
    private Instant createAt;

    @Column("update_at")
    private Instant updateAt;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
