package com.example.webflux.infrastructure.auth.persistence;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * ¿ Porque persistable?
 * 
 * Unos de los problemas que me he encontrado es decidir si yo debo manejar los
 * identificadores unicos
 * de los registros de cada tabla de base de datos, no podia usar las dos
 * alternativas pretender que la base
 * de datos me genere un id al yo hacer solicitar el guardado de un registro
 * 
 * otro problema son los mappers, que son clases transformadoras que permite en
 * mi caso, transformar de domain a
 * entidad o viceversa, un problema es que en las id de los objetos o registros
 * que pretendia guardar en la base
 * de datos siempre llevaban un valor de null lo cual explicaré en el siguiente
 * parrafo.
 * 
 * R2DBC como especificacion no bloqueante para acceso a bases de datos tiene un
 * inconveniente que como
 * nosotros como desarrolladores debemos tener en cuenta y es si partimos del
 * hecho de guardar un registro
 * en la base de datos y ese objeto en su campo @Id seteamos un valor r2dbc lo
 * interpreta como un UPDATE
 * porque entiende que si este registro tiene una primary key(PK) debe existir
 * en la tabla, caso contrario
 * donde nosotros seteamos NULL en la id, r2dbc interpretará que ese registro no
 * existe, entonces lo interpreta
 * como INSERT, por eso es que cuando mandamos null en el registro actual que
 * queremos guardar la base de datos
 * si le haz especificado una generacion de id el como resposable le asignara
 * una id a ese registro.
 * 
 * 
 * ¿Que soluciona PERSISTABLE?
 * 
 * Nos da acceso total como desarrolladores al proceso de guardado de la base de
 * datos (INSERT / UPDATE)
 * por eso debemos implementar la interfaz persistable y con su respectivo tipo
 * de dato de la llave primaria
 * 
 * al implementarlo nos obliga a sobreescribir el metodo isNew este metodo que
 * es un booleano permitira
 * elegir entre UPDATE y INSERT dependiendo si es false o true
 */

@Table(name = "users")
@Data
public class UserModelEntity implements Persistable<UUID> {
    @Id
    @Column("user_id")
    private UUID id;

    @Transient
    private boolean isNew = true; // FALSE por defecto asi que lo inicializamos en TRUE -> Insert

    @Column("username")
    private String username;

    @Column("auth_status")
    private String authStatus;

    @Column("email")
    private String email;

    @Column("password_hash")
    private String passwordHash;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void markNotNew() {
        this.isNew = false;
    }
}
