package com.example.webflux.infrastructure.emailVerificationToken.mapper;

import com.example.webflux.domain.emailVerificationToken.models.EmailVerificationTokenModel;
import com.example.webflux.infrastructure.emailVerificationToken.persistence.EmailVerificationTokenEntity;

public class EmailVerificationTokenMapper {

    public static EmailVerificationTokenModel toDomain(EmailVerificationTokenEntity entity) {
        return EmailVerificationTokenModel.create(entity.getId(), entity.getUserId(), entity.getTokenHash(),
                entity.getExpiredAt(),
                entity.getConsumedAt(), entity.getCreateAt());
    }

    public static EmailVerificationTokenEntity toEntity(EmailVerificationTokenModel domain) {
        EmailVerificationTokenEntity entity = new EmailVerificationTokenEntity();

        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        entity.setTokenHash(domain.getTokenHash());
        entity.setExpiredAt(domain.getExpiredAt());
        entity.setCreateAt(domain.getCreateAt());
        entity.setConsumedAt(domain.getConsumedAt());

        return entity;
    }
}
