package com.example.webflux.infrastructure.Autorization.persistence.permissions;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("permissions")
@Data
public class PermissionModelEntity implements Persistable<UUID> {

    @Id
    @Column("permission_id")
    private UUID id;

    @Transient
    private Boolean isNew = true;

    @Column("permission_name")
    private String permissionName;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
