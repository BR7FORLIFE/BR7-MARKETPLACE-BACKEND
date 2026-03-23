package com.example.webflux.application.auth.dtos.request;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

public record Me(UUID userId, String username, Collection<? extends GrantedAuthority> authorities) {

}
