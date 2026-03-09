package com.example.webflux.infrastructure.auth.mapper;

import com.example.webflux.domain.auth.models.UserAuthStatus;
import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.infrastructure.auth.persistence.UserModelEntity;

public class UserMapper {

    public static UserModelDomain toDomain(UserModelEntity userModel) {
        return UserModelDomain.createNew(userModel.getId(), userModel.getUsername(),
                UserAuthStatus.valueOf(userModel.getAuthStatus()),
                userModel.getEmail(), userModel.getPasswordHash());
    }

    public static UserModelEntity toEntity(UserModelDomain userModel) {
        UserModelEntity userModelEntity = new UserModelEntity();

        /**
         * 
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

        userModelEntity.setId(userModel.getId());
        userModelEntity.setUsername(userModel.getUsername());
        userModelEntity.setEmail(userModel.getEmail());
        userModelEntity.setPasswordHash(userModel.getPassword());
        userModelEntity.setAuthStatus(userModel.getAuthStatus().name());
        return userModelEntity;
    }
}
