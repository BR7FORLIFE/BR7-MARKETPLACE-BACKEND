package com.example.webflux.infrastructure.emailVerificationToken.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table(name = "email_verification_token")
public class EmailVerificationTokenEntity implements Persistable<UUID> {

    @Id
    @Column("email_verification_token_id")
    private UUID id;

    @Transient
    private boolean isNew = true; // para poder hacer los INSERTs

    @Column("user_id")
    private UUID userId;

    @Column("token_hash")
    private String tokenHash;

    @Column("expired_at")
    private Instant expiredAt;

    @Column("consumed_at")
    private Instant consumedAt;

    @Column("create_at")
    private Instant createAt;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
