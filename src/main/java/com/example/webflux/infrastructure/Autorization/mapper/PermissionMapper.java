package com.example.webflux.infrastructure.Autorization.mapper;

import com.example.webflux.domain.Authorization.models.permissions.PermissionsModelDomain;
import com.example.webflux.infrastructure.Autorization.persistence.permissions.PermissionModelEntity;

public class PermissionMapper {

    public static PermissionsModelDomain toDomain(PermissionModelEntity entity) {
        return PermissionsModelDomain.createNew(entity.getId(), entity.getPermissionName());
    }

    public static PermissionModelEntity toEntity(PermissionsModelDomain domain) {
        PermissionModelEntity entity = new PermissionModelEntity();

        entity.setId(domain.getPermissionId());
        entity.setPermissionName(domain.getPermissionName());

        return entity;
    }
}
