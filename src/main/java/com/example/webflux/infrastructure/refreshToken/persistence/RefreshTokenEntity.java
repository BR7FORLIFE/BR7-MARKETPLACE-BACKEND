package com.example.webflux.infrastructure.refreshToken.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "refresh_token")
@Data
public class RefreshTokenEntity implements Persistable<UUID> {
    @Id
    @Column("refresh_token_id")
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Transient
    private Boolean isNew = true;

    @Column("token_hash")
    private String tokenHash;

    @Column("expired_at")
    private Instant expiredAt;

    @Column("create_at")
    private Instant createAt;

    @Column("revoked")
    private boolean revoked;

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
