package com.example.webflux.infrastructure.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.webflux.infrastructure.security.jwt.JwtService;
import com.nimbusds.jose.JOSEException;

import reactor.core.publisher.Mono;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtReactiveAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @SuppressWarnings(value = "unchecked")
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        return jwtService.validateAccessToken(token)
                .flatMap(claims -> {
                    String subject = claims.getSubject();
                    List<String> rols = (List<String>) claims.getClaim("ROLS");
                    List<GrantedAuthority> authorities = rols == null ? List.of()
                            : rols.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                    Authentication auth = new UsernamePasswordAuthenticationToken(subject, token, authorities);
                    return Mono.just(auth);
                }).onErrorResume(e -> {
                    return Mono.error(new JOSEException("the token is not valid"));
                });
    }
}
