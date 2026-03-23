package com.example.webflux.infrastructure.security;

import java.util.List;
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
    @SuppressWarnings("unchecked")
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

                    List<String> rols = (List<String>) claims.getClaim("ROLS");
                    List<GrantedAuthority> authorities = rols == null
                            ? List.of()
                            : rols.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                    CustomUserDetails userDetails = new CustomUserDetails(
                            UserModelDomain.createNew(UUID.fromString(userId), username, authStatus, null, null),
                            authorities);

                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
                    return Mono.just(auth);
                })
                .onErrorResume(e -> Mono.empty());
    }

}
