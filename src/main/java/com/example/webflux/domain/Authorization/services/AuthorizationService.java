package com.example.webflux.domain.Authorization.services;

import java.util.List;

public class AuthorizationService {
    /**
     * Este servicio nos va a permitir saber si en una lista de roles
     * contiene roles provilegiados para hacer una accion
     */

    public static Boolean canViewAll(List<String> roles) {
        return roles.contains("ADMIN");
    }
}
