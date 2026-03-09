package com.example.webflux.infrastructure.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.webflux.domain.auth.models.UserModelDomain;

public final class CustomUserDetails implements UserDetails {

    private UUID userId;
    private String username;
    private String email;
    private String password;
    private String authStatus;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserModelDomain userModel, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userModel.getId();
        this.username = userModel.getUsername();
        this.email = userModel.getEmail();
        this.password = userModel.getPassword();
        this.authStatus = userModel.getAuthStatus().name();
        this.authorities = authorities;
    }

    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public String getEmail() {
        return email;
    }
}
