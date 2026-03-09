package com.example.webflux.application.products.commands;

import java.util.UUID;

public record RegisterProductCommandResult(UUID productId, String name) {

}
