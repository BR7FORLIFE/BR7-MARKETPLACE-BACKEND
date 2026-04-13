package com.example.webflux.infrastructure.security;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.webflux.domain.auth.models.UserAuthStatus;
import com.example.webflux.domain.auth.models.UserModelDomain;
import com.example.webflux.infrastructure.security.jwt.JwtService;

import reactor.core.publisher.Mono;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    public JwtReactiveAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        return jwtService.validateAccessToken(token)
                .flatMap(claims -> {
                    String username = claims.getSubject();

                    String userId;
                    UserAuthStatus authStatus;
                    try {
                        userId = claims.getClaimAsString("userId");
                        authStatus = UserAuthStatus.valueOf(claims.getClaimAsString("authStatus"));
                    } catch (Exception e) {
                        return Mono.<Authentication>error(new BadCredentialsException("Invalid JWT claim: userId"));
                    }

                    List<String> rols;
                    try {
                        rols = Optional.ofNullable(claims.getStringListClaim("ROLS"))
                                .orElse(List.of());
                    } catch (ParseException e) {
                        return Mono.error(new BadCredentialsException("Invalid roles in JWT", e));
                    }

                    List<GrantedAuthority> authorities = rols.stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toList());

                    CustomUserDetails userDetails = new CustomUserDetails(
                            UserModelDomain.createNew(UUID.fromString(userId), username, authStatus, null, null),
                            authorities);

                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
                    return Mono.just(auth);
                })
                .onErrorResume(e -> Mono.empty());
    }

}
