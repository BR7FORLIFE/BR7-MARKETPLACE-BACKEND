package com.example.webflux.infrastructure.security.adapters;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.webflux.application.auth.model.AuthenticatedUser;
import com.example.webflux.application.auth.ports.UserJwtPort;
import com.example.webflux.domain.auth.models.UserAuthStatus;
import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.infrastructure.security.CustomUserDetails;
import com.example.webflux.infrastructure.security.jwt.JwtService;

import reactor.core.publisher.Mono;

@Component
public class UserJwtAdapter implements UserJwtPort {

    private final JwtService jwtService;

    public UserJwtAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<String> generateAccessToken(AuthenticatedUser user) {
        Collection<? extends GrantedAuthority> authorities = user.rols()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        UserDetails details = new CustomUserDetails(
                UserModelDomain.createNew(
                        user.userId(),
                        user.username(),
                        UserAuthStatus.valueOf(user.authStatus()),
                        user.email(),
                        user.password()),
                authorities);

        return jwtService.generateAccessToken(details);
    }

}
