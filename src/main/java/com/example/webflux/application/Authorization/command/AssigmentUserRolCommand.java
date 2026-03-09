package com.example.webflux.application.Authorization.command;

import java.util.UUID;

public record AssigmentUserRolCommand(UUID userId, String role) {

}
