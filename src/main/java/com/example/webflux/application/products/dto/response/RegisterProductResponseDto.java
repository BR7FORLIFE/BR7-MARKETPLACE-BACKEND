package com.example.webflux.application.products.dto.response;

import java.util.UUID;

public record RegisterProductResponseDto(UUID productId, String name, String message) {

}
