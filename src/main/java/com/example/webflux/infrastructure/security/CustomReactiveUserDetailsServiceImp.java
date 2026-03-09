package com.example.webflux.infrastructure.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.webflux.infrastructure.Autorization.repository.R2dbcRolRepositoryAdapter;
import com.example.webflux.infrastructure.auth.repository.R2dbcUserRepositoryAdapter;
import com.example.webflux.infrastructure.security.ports.CustomReactiveUserDetailsService;

import reactor.core.publisher.Mono;

@Service
public class CustomReactiveUserDetailsServiceImp implements CustomReactiveUserDetailsService {

    private final R2dbcUserRepositoryAdapter r2dbcUserRepositoryAdapter;
    private final R2dbcRolRepositoryAdapter r2dbcRolRepositoryAdapter;

    public CustomReactiveUserDetailsServiceImp(R2dbcUserRepositoryAdapter r2dbcUserRepositoryAdapter,
            R2dbcRolRepositoryAdapter r2dbcRolRepositoryAdapter) {
        this.r2dbcUserRepositoryAdapter = r2dbcUserRepositoryAdapter;
        this.r2dbcRolRepositoryAdapter = r2dbcRolRepositoryAdapter;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return r2dbcUserRepositoryAdapter.findByUsername(username)
                .flatMap(user -> {
                    return r2dbcRolRepositoryAdapter.findRols(user.getId())
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getUserRol()))
                            .collectList()
                            .map(roles -> new CustomUserDetails(user, roles));
                });
    }

    public Mono<UserDetails> findByEmail(String email) {
        return r2dbcUserRepositoryAdapter.findByEmail(email)
                .flatMap(user -> {
                    return r2dbcRolRepositoryAdapter.findRols(user.getId())
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getUserRol()))
                            .collectList()
                            .map(roles -> new CustomUserDetails(user, roles));
                });
    }

}
