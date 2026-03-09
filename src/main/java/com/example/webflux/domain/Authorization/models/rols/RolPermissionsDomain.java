package com.example.webflux.domain.Authorization.models.rols;

public final class RolPermissionsDomain {

    private final String rolId;
    private final String permissionId;

    public RolPermissionsDomain(String rolId, String permissionId) {
        this.rolId = rolId;
        this.permissionId = permissionId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public String getRolId() {
        return rolId;
    }
}
