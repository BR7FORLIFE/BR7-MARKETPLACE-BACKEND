package com.example.webflux.domain.listings.services;

import java.util.List;

public class ListingService {

    /**
     * Este servicio nos va a permitir saber si en una lista de roles
     * contiene roles provilegiados para hacer una accion
     */

    public static Boolean canViewAll(List<String> roles) {
        return roles.contains("ADMIN");
    }
}
