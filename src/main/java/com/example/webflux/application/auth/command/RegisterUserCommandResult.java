package com.example.webflux.application.auth.command;

import java.util.UUID;

public record RegisterUserCommandResult(UUID user_id, String username, String message) {

}
