package com.example.webflux.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.example.webflux.infrastructure.security.JwtReactiveAuthenticationManager;
import com.example.webflux.infrastructure.security.jwt.JwtService;

@Configuration
@EnableReactiveMethodSecurity()
public class SecurityConfig {

    private final JwtService jwtService;

    private final String[] PUBLIC_PATHS = { "/auth/**", "/email/**" };

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationWebFilter jwtAuthFilter)
            throws Exception {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        exchange -> exchange.pathMatchers(PUBLIC_PATHS).permitAll().anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public AuthenticationWebFilter jwtAuthFilter(
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            ServerAuthenticationConverter bearerConverter) {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(reactiveAuthenticationManager);

        filter.setServerAuthenticationConverter(bearerConverter);
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService reactiveUserDetailsService,
            PasswordEncoder passwordEncoder) throws Exception {
        return new JwtReactiveAuthenticationManager(jwtService); // -> implementacion de ReactiveUserDetailsService
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}