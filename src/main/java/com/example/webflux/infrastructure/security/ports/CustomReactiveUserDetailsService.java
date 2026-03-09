package com.example.webflux.infrastructure.security.ports;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import reactor.core.publisher.Mono;

public interface CustomReactiveUserDetailsService extends ReactiveUserDetailsService {
    public Mono<UserDetails> findByUsername(String username);

    public Mono<UserDetails> findByEmail(String email);
}
