package com.example.webflux.domain.refreshToken.models;

import java.time.Instant;
import java.util.UUID;

public final class RefreshTokenModel {
    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final Instant expiresAt;
    private final boolean revoked;
    private final Instant createAt;

    private RefreshTokenModel(UUID id, UUID userId, String tokenHash, Instant expiresAt, boolean revoked,
            Instant createAt) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
        this.createAt = createAt;
    }

    public static RefreshTokenModel createNew(UUID id, UUID userId, String tokenHash, Instant expiresAt,
            Instant createAt) {
        return new RefreshTokenModel(id, userId, tokenHash, expiresAt, false, createAt);
    }

    public static RefreshTokenModel createDraft(UUID userId, String tokenHash, Instant expiresAt,
            Instant createAt) {
        return new RefreshTokenModel(UUID.randomUUID(), userId, tokenHash, expiresAt, false, createAt);
    }

    public RefreshTokenModel revoke() {
        if (this.revoked) {
            return this;
        }

        return new RefreshTokenModel(
                this.id,
                this.userId,
                this.tokenHash,
                this.expiresAt,
                true,
                this.createAt);
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiresAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Instant getCreateAt() {
        return createAt;
    }

}
