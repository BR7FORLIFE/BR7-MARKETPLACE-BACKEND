package com.example.webflux.application.auth.dtos.response;

import java.util.UUID;

public record RegisterUserResponseDto(UUID user_id, String username) {

}
