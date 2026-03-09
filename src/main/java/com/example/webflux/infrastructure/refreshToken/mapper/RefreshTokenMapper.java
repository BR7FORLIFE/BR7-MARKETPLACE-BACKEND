package com.example.webflux.infrastructure.refreshToken.mapper;

import com.example.webflux.domain.refreshToken.models.RefreshTokenModel;
import com.example.webflux.infrastructure.refreshToken.persistence.RefreshTokenEntity;

public class RefreshTokenMapper {

    public static RefreshTokenModel toDomain(RefreshTokenEntity refreshTokenEntity) {
        return RefreshTokenModel.createNew(refreshTokenEntity.getId(), refreshTokenEntity.getUserId(),
                refreshTokenEntity.getTokenHash(), refreshTokenEntity.getExpiredAt(), refreshTokenEntity.getCreateAt());
    }

    public static RefreshTokenEntity toEntity(RefreshTokenModel refreshTokenModel) {
        RefreshTokenEntity entity = new RefreshTokenEntity();

        /**
         * Spring Data R2DBC decide entre INSERT y UPDATE únicamente en función del
         * estado del identificador de la entidad. Si la entidad se persiste con un
         * campo anotado con @Id no nulo, el framework asume que el registro ya existe y
         * ejecuta un UPDATE; si el identificador es null, ejecuta un INSERT. R2DBC no
         * verifica previamente la existencia del registro en la base de datos, ya que
         * no mantiene un contexto de persistencia ni realiza consultas adicionales. Por
         * este motivo, al crear nuevas entidades, el identificador no debe asignarse
         * manualmente y debe ser generado por la base de datos; de lo contrario, Spring
         * Data intentará actualizar una fila inexistente y la operación fallará.
         */
        entity.setId(refreshTokenModel.getId());
        entity.setUserId(refreshTokenModel.getUserId());
        entity.setTokenHash(refreshTokenModel.getTokenHash());
        entity.setExpiredAt(refreshTokenModel.getExpiresAt());
        entity.setRevoked(refreshTokenModel.isRevoked());
        entity.setCreateAt(refreshTokenModel.getCreateAt());

        return entity;
    }
}
