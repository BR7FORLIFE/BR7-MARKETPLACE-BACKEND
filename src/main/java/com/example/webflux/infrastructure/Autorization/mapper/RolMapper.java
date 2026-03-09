package com.example.webflux.infrastructure.Autorization.mapper;

import com.example.webflux.domain.Authorization.models.rols.RolModelDomain;
import com.example.webflux.infrastructure.Autorization.persistence.rols.RolModelEntity;

public class RolMapper {

    public static RolModelDomain toDomain(RolModelEntity entity) {
        return RolModelDomain.createNew(entity.getId(), entity.getRol());
    }

    public static RolModelEntity toEntity(RolModelDomain domain) {
        RolModelEntity entity = new RolModelEntity();

        entity.setId(domain.getId());
        entity.setRol(domain.getUserRol());

        return entity;
    }
}
