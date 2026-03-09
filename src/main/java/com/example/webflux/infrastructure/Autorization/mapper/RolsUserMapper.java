package com.example.webflux.infrastructure.Autorization.mapper;

import com.example.webflux.domain.Authorization.models.rols.RolsUsersDomain;
import com.example.webflux.infrastructure.Autorization.persistence.rols.RolsUsersEntity;

public class RolsUserMapper {

    public static RolsUsersDomain toDomain(RolsUsersEntity entity) {
        return RolsUsersDomain.createNew(entity.getRolId(), entity.getUserId());
    }

    public static RolsUsersEntity toEntity(RolsUsersDomain domain) {
        RolsUsersEntity entity = new RolsUsersEntity();

        entity.setRolId(domain.getRolId());
        entity.setUserId(domain.getUserId());

        return entity;
    }
}
