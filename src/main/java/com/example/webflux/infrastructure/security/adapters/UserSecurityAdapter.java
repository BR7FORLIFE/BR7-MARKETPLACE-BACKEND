package com.example.webflux.infrastructure.security.adapters;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.webflux.application.auth.model.AuthenticatedUser;
import com.example.webflux.application.auth.ports.UserSecurityPort;
import com.example.webflux.infrastructure.security.CustomUserDetails;
import com.example.webflux.infrastructure.security.ports.CustomReactiveUserDetailsService;

import reactor.core.publisher.Mono;

@Component
public class UserSecurityAdapter implements UserSecurityPort {

    private final CustomReactiveUserDetailsService customReactiveUserDetailsService;

    public UserSecurityAdapter(CustomReactiveUserDetailsService customReactiveUserDetailsService) {
        this.customReactiveUserDetailsService = customReactiveUserDetailsService;
    }

    @Override
    public Mono<AuthenticatedUser> findByUsername(String username) {
        return customReactiveUserDetailsService.findByUsername(username)
                .map(this::mapToAuthenticatedUser);
    }

    @Override
    public Mono<AuthenticatedUser> findByEmail(String email) {

        return customReactiveUserDetailsService.findByEmail(email)
                .map(this::mapToAuthenticatedUser);
    }

    private AuthenticatedUser mapToAuthenticatedUser(UserDetails userDetails) {
        CustomUserDetails user = (CustomUserDetails) userDetails;

        return new AuthenticatedUser(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAuthStatus(),
                user.getAuthorities()
                        .stream()
                        .map(rol -> rol.getAuthority().replaceFirst("^ROLE_", ""))
                        .toList());
    }
}
