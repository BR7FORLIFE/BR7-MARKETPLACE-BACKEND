package com.example.webflux.application.products.commands;

import java.util.UUID;

public record GetProductByIdCommand(UUID productId, UUID userId) {

}
