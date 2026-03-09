package com.example.webflux.infrastructure.Autorization.persistence.rols;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("rols")
@Data
public class RolModelEntity implements Persistable<UUID> {

    @Id
    @Column("rol_id")
    private UUID id;

    @Transient
    private Boolean isNew = true;

    @Column("rol")
    private String rol;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
