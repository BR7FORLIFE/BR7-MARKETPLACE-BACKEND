package com.example.webflux.infrastructure.Autorization.persistence.rols;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table("users_rols")
@Data
public class RolsUsersEntity {

    @Id
    @Column("user_rol_id")
    private Integer userRolId;

    @Column("user_id")
    private UUID userId;

    @Column("rol_id")
    private UUID rolId;
}
