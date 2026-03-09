package com.example.webflux.domain.auth.models;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

public final class UserModelDomain {
    private final UUID id;
    private final String username;
    private final String email;
    private final String passwordHash;
    private final UserAuthStatus authStatus;

    private UserModelDomain(UUID id, String username, UserAuthStatus status, String email, String password) {
        this.id = id;
        this.username = username;
        this.authStatus = status;
        this.email = email;
        this.passwordHash = password;
    }

    public static UserModelDomain createNew(UUID id, String username, UserAuthStatus status, String email,
            String password) {
        return new UserModelDomain(id, username, status, email, password);
    }

    public static UserModelDomain createDraft(String username, UserAuthStatus status, String email,
            String password) {
        return new UserModelDomain(UUID.randomUUID(), username, status, email, password);
    }

    public static UserModelDomain register(String username, String email, String passwordHash) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("the username is null!");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("the email is null!");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("not password!");
        }

        return new UserModelDomain(UUID.randomUUID(), username, UserAuthStatus.PENDING, email, passwordHash);
    }

    public UserModelDomain activateUser() {
        if (this.authStatus == UserAuthStatus.ACTIVE) {
            throw new IllegalStateException("User already activate");
        }
        return new UserModelDomain(
                id,
                username,
                UserAuthStatus.ACTIVE,
                email,
                passwordHash);
    }

    public boolean passwordMatches(String raw, PasswordEncoder encoder) {
        return encoder.matches(raw, passwordHash);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return passwordHash;
    }

    public UserAuthStatus getAuthStatus() {
        return authStatus;
    }
}
