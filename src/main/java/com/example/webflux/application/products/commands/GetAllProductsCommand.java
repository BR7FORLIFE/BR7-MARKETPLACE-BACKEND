package com.example.webflux.application.products.commands;

import java.time.Instant;
import java.util.UUID;

public record GetAllProductsCommand(UUID userId, int limit, int offset, String orden, Instant updateAt) {

}
