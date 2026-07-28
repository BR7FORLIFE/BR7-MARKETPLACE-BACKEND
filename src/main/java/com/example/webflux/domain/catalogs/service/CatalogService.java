package com.example.webflux.domain.catalogs.service;

import java.util.UUID;

public class CatalogService {

    public static String generateCodeCatalog() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    };
}
