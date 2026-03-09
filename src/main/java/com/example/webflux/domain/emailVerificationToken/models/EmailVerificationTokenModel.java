package com.example.webflux.domain.emailVerificationToken.models;

import java.time.Instant;
import java.util.UUID;

import com.example.webflux.domain.emailVerificationToken.exceptions.EmailConsumedException;
import com.example.webflux.domain.emailVerificationToken.exceptions.EmailExpiredException;

public final class EmailVerificationTokenModel {
    private final UUID id;
    private final UUID userId;
    private final String tokenHash;
    private final Instant expiredAt;
    private final Instant consumedAt;
    private final Instant createAt;

    private EmailVerificationTokenModel(UUID id, UUID userId, String tokenHash, Instant expiredAt, Instant consumedAt,
            Instant createAt) {
        this.id = id;
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiredAt = expiredAt;
        this.consumedAt = consumedAt;
        this.createAt = createAt;
    }

    public static EmailVerificationTokenModel createNew(UUID userId, String tokenHash, Instant expiredAt,
            Instant consumedAt,
            Instant createAt) {
        return new EmailVerificationTokenModel(UUID.randomUUID(), userId, tokenHash, expiredAt, consumedAt, createAt);
    }

    public static EmailVerificationTokenModel create(UUID id, UUID userId, String tokenHash, Instant expiredAt,
            Instant consumedAt, Instant createAt) {
        return new EmailVerificationTokenModel(id, userId, tokenHash, expiredAt, consumedAt, createAt);
    }

    // reglas de negocio que hay que implementar para veficar el email

    /**
     * 
     * 1. Validar si el token de verificacion de email esta expirado
     * 2. Validar si el token fue consumido
     * 3. Activar el usuario si todo esta correcto
     */
    public EmailVerificationTokenModel consume(Instant now) {

        if (this.expiredAt.isBefore(now)) {
            throw new EmailExpiredException();
        }

        if (this.consumedAt != null) {
            throw new EmailConsumedException();
        }

        return new EmailVerificationTokenModel(
                this.id,
                this.userId,
                this.tokenHash,
                this.expiredAt,
                now,
                this.createAt);

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

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public Instant getConsumedAt() {
        return consumedAt;
    }

    public Instant getCreateAt() {
        return createAt;
    }

}
