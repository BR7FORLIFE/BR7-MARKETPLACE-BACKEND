package com.example.webflux.domain.AssetsMedia.models;

import java.time.Instant;
import java.util.UUID;

public final class AssetsMediaModel {

    private final UUID assetsMediaId;
    private final UUID ownerId;
    private final OwnerMediaType ownerType;
    private final String cloudPublicId;
    private final String format;
    private final String resourceType;
    private final String storageType;
    private final Integer numberBytes;
    private final Integer width;
    private final Integer height;
    private final String url;
    private final String secureUrl;
    private final Integer position;
    private final Boolean isCover;
    private final Instant createAt;
    private final Instant deleteAt;

    private AssetsMediaModel(UUID assetsMediaId, UUID ownerId, OwnerMediaType ownerType, String cloudPublicId, String format,
            String resourceType, String storageType, Integer numberBytes, Integer width, Integer height, String url,
            String secureUrl, Integer position, Boolean isCover, Instant createAt, Instant deleteAt) {
        this.assetsMediaId = assetsMediaId;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.cloudPublicId = cloudPublicId;
        this.format = format;
        this.resourceType = resourceType;
        this.storageType = storageType;
        this.numberBytes = numberBytes;
        this.width = width;
        this.height = height;
        this.url = url;
        this.secureUrl = secureUrl;
        this.position = position;
        this.isCover = isCover;
        this.createAt = createAt;
        this.deleteAt = deleteAt;
    }

    public static AssetsMediaModel createNew(UUID assetsMediaId, UUID ownerId, OwnerMediaType ownerType, String cloudPublicId,
            String format,
            String resourceType, String storageType, Integer numberBytes, Integer width, Integer height, String url,
            String secureUrl, Integer position, Boolean isCover, Instant createAt, Instant deleteAt) {

        return new AssetsMediaModel(assetsMediaId, ownerId, ownerType, cloudPublicId, format, resourceType, storageType,
                numberBytes, width, height, url, secureUrl, position, isCover, createAt, deleteAt);
    }

    public UUID getAssetsMediaId() {
        return assetsMediaId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public OwnerMediaType getOwnerType() {
        return ownerType;
    }

    public String getCloudPublicId() {
        return cloudPublicId;
    }

    public String getFormat() {
        return format;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getStorageType() {
        return storageType;
    }

    public Integer getNumberBytes() {
        return numberBytes;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public Integer getPosition() {
        return position;
    }

    public Boolean getIsCover() {
        return isCover;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getDeleteAt() {
        return deleteAt;
    }

}
