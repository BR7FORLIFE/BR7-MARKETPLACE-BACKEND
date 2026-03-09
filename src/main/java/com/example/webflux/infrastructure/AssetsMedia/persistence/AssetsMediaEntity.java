package com.example.webflux.infrastructure.AssetsMedia.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("asset_media")
@Data
public class AssetsMediaEntity {

    @Id
    private UUID id;

    @Column("owner_id")
    private UUID ownerId;

    @Column("owner_type")
    private String ownerType;

    @Column("cloud_public_id")
    private String cloudPublicId;

    @Column("format")
    private String format;

    @Column("resource_type")
    private String resourceType;

    @Column("storage_type")
    private String storageType;

    @Column("bytes")
    private Integer numberbytes;

    @Column("width")
    private Integer width;

    @Column("height")
    private Integer height;

    @Column("url")
    private String url;

    @Column("secure_url")
    private String secureUrl;

    @Column("position")
    private Integer position;

    @Column("is_cover")
    private Boolean isCover;

    @Column("created_at")
    private Instant createAt;

    @Column("deleted_at")
    private Instant deleteAt;
}