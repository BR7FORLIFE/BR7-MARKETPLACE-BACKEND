package com.example.webflux.domain.Authorization.models.rols;

import java.util.UUID;

public final class RolModelDomain {
    private final UUID id;
    private final String role;

    private RolModelDomain(UUID id, String role) {
        this.id = id;
        this.role = role;
    }

    public static RolModelDomain createNew(UUID id, String role) {
        return new RolModelDomain(id, role);
    }

    public static RolModelDomain createDraft(String role) {
        return new RolModelDomain(UUID.randomUUID(), role);
    }

    public UUID getId() {
        return id;
    }

    public String getUserRol() {
        return this.role;
    }
}
