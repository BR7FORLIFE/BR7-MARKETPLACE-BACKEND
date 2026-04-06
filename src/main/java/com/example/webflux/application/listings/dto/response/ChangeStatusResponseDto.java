package com.example.webflux.application.listings.dto.response;

import java.time.Instant;

public record ChangeStatusResponseDto(String message, Instant updateAt) {

}
