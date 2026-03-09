package com.example.webflux.infrastructure.AssetsMedia.mapper;

import com.example.webflux.domain.AssetsMedia.models.AssetsMediaModel;
import com.example.webflux.domain.AssetsMedia.models.OwnerMediaType;
import com.example.webflux.infrastructure.AssetsMedia.persistence.AssetsMediaEntity;

public class AssetsMediaMapper {

    public static AssetsMediaModel toDomain(AssetsMediaEntity assetsMediaEntity) {
        return AssetsMediaModel.createNew(
                assetsMediaEntity.getId(),
                assetsMediaEntity.getOwnerId(),
                OwnerMediaType.valueOf(assetsMediaEntity.getOwnerType()),
                assetsMediaEntity.getCloudPublicId(),
                assetsMediaEntity.getFormat(),
                assetsMediaEntity.getResourceType(),
                assetsMediaEntity.getStorageType(),
                assetsMediaEntity.getNumberbytes(),
                assetsMediaEntity.getWidth(),
                assetsMediaEntity.getHeight(),
                assetsMediaEntity.getUrl(),
                assetsMediaEntity.getSecureUrl(),
                assetsMediaEntity.getPosition(),
                assetsMediaEntity.getIsCover(),
                assetsMediaEntity.getCreateAt(),
                assetsMediaEntity.getDeleteAt());
    }

    public static AssetsMediaEntity toEntity(AssetsMediaModel assetsMediaModel) {
        AssetsMediaEntity entity = new AssetsMediaEntity();
        entity.setId(assetsMediaModel.getAssetsMediaId());
        entity.setOwnerId(assetsMediaModel.getOwnerId());
        entity.setOwnerType(assetsMediaModel.getOwnerType().toString());
        entity.setCloudPublicId(assetsMediaModel.getCloudPublicId());
        entity.setFormat(assetsMediaModel.getFormat());
        entity.setResourceType(assetsMediaModel.getResourceType());
        entity.setStorageType(assetsMediaModel.getStorageType());
        entity.setNumberbytes(assetsMediaModel.getNumberBytes());
        entity.setWidth(assetsMediaModel.getWidth());
        entity.setHeight(assetsMediaModel.getHeight());
        entity.setUrl(assetsMediaModel.getUrl());
        entity.setSecureUrl(assetsMediaModel.getSecureUrl());
        entity.setPosition(assetsMediaModel.getPosition());
        entity.setIsCover(assetsMediaModel.getIsCover());
        entity.setCreateAt(assetsMediaModel.getCreateAt());
        entity.setDeleteAt(assetsMediaModel.getDeleteAt());

        return entity;
    }
}
