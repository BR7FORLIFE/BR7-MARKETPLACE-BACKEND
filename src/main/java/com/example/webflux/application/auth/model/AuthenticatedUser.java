package com.example.webflux.application.auth.model;

import java.util.Collection;
import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String username,
        String email,
        String password,
        String authStatus,
        Collection<String> rols) {

}
