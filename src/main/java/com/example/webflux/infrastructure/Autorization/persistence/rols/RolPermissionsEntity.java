package com.example.webflux.infrastructure.Autorization.persistence.rols;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("rol_permissions")
@Data
public class RolPermissionsEntity {
    @Id
    @Column("rol_permission_id")
    private Integer rolPermission;

    @Column("rol_id")
    private UUID rolId;

    @Column("permission_id")
    private UUID permissionId;
}
