package com.example.webflux.application.products.commands;

import java.util.UUID;

public record RegisterProductCommand(UUID userId, String sku, String name, String shortDescription,
        String longDescription, String model) {

}
