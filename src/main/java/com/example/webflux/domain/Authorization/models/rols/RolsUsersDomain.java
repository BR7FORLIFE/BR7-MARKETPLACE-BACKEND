package com.example.webflux.domain.Authorization.models.rols;

import java.util.UUID;

public final class RolsUsersDomain {
    private final UUID rolId;
    private final UUID userId;

    private RolsUsersDomain(UUID rolId, UUID userId) {
        this.rolId = rolId;
        this.userId = userId;
    }

    public static RolsUsersDomain createNew(UUID rolId, UUID userId) {
        return new RolsUsersDomain(rolId, userId);
    }

    public UUID getRolId() {
        return rolId;
    }

    public UUID getUserId() {
        return userId;
    }
}
