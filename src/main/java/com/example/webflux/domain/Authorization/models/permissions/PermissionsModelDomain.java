package com.example.webflux.domain.Authorization.models.permissions;

import java.util.UUID;

public final class PermissionsModelDomain {
    private final UUID permissionId;
    private final String permissionName;

    private PermissionsModelDomain(UUID permissionId, String permissionName) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    public static PermissionsModelDomain createNew(UUID permissionId, String permissionName) {
        return new PermissionsModelDomain(permissionId, permissionName);
    }

    public static PermissionsModelDomain createDraft(String permissionName) {
        return new PermissionsModelDomain(UUID.randomUUID(), permissionName);
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }
}