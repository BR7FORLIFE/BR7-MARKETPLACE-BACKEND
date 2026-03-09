package com.example.webflux.application.products.dto;

import java.util.UUID;

public record RegisterProductResponseDto(UUID productId, String name, String message) {

}
