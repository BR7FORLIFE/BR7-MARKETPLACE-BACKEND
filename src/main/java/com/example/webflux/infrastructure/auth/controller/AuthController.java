package com.example.webflux.infrastructure.auth.controller;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.auth.command.LoginUserCommand;
import com.example.webflux.application.auth.command.RegisterUserCommand;
import com.example.webflux.application.auth.dtos.request.LoginUserRequestDto;
import com.example.webflux.application.auth.dtos.request.Me;
import com.example.webflux.application.auth.dtos.request.RegisterUserRequestDto;
import com.example.webflux.application.auth.dtos.response.AccessLoginUserResponseDto;
import com.example.webflux.application.auth.dtos.response.RegisterUserResponseDto;
import com.example.webflux.application.auth.exceptions.UserNotAuthenticate;
import com.example.webflux.application.auth.usecases.AuthUseCase;
import com.example.webflux.application.refreshToken.dtos.RefreshTokenDtoResponse;
import com.example.webflux.application.refreshToken.usecases.RefreshTokenUseCase;
import com.example.webflux.infrastructure.security.CustomUserDetails;

import io.netty.handler.codec.http.cookie.CookieHeaderNames.SameSite;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final AuthUseCase authUseCase;

    public AuthController(RefreshTokenUseCase refreshTokenUseCase, AuthUseCase authUseCase) {
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterUserResponseDto>> register(@RequestBody @Valid RegisterUserRequestDto user) {
        RegisterUserCommand cmd = new RegisterUserCommand(user.username(), user.password(), user.email());

        return authUseCase.executeRegister(cmd)
                .map(result -> ResponseEntity.ok()
                        .body(new RegisterUserResponseDto(result.user_id(), result.username())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AccessLoginUserResponseDto>> login(@Valid @RequestBody LoginUserRequestDto body,
            ServerHttpResponse response) {

        String email = body.email();
        String password = body.password();

        LoginUserCommand cmd = new LoginUserCommand(email, password);

        return authUseCase.executeLogin(cmd)
                .map(token -> {
                    ResponseCookie cookie = ResponseCookie
                            .from("refresh_token", token.refreshRaw())
                            .httpOnly(true)
                            .secure(true) // IMPORTANTE QUITARLO EL VALOR FALSE EN PRODUCCION
                            .path("/api/auth")
                            .maxAge(30 * 24 * 60 * 60)
                            .sameSite(SameSite.Strict.toString())
                            .build();

                    response.addCookie(cookie);

                    AccessLoginUserResponseDto access_response = new AccessLoginUserResponseDto(token.accessToken());
                    return ResponseEntity.ok().body(access_response);
                });
    }

    @PostMapping("/refresh/rotate")
    public Mono<ResponseEntity<RefreshTokenDtoResponse>> rotate(ServerHttpRequest request,
            ServerHttpResponse response) {
        HttpCookie cookie = request.getCookies().getFirst("refresh_token");
        if (cookie == null)
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        String raw = cookie.getValue();
        return refreshTokenUseCase.validateAndRotate(raw)
                .flatMap(newRefreshToken -> {
                    ResponseCookie newCookie = ResponseCookie
                            .from("refresh_token", newRefreshToken)
                            .httpOnly(true)
                            .secure(true)
                            .path("/api/auth")
                            .maxAge(30 * 24 * 60 * 60)
                            .sameSite(SameSite.Strict.toString())
                            .build();

                    response.addCookie(newCookie);

                    RefreshTokenDtoResponse refreshResponse = new RefreshTokenDtoResponse(newRefreshToken);
                    return Mono.just(ResponseEntity.ok().body(refreshResponse));
                });

    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<AccessLoginUserResponseDto>> refresh(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("refresh_token");
        if (cookie == null) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }

        String refreshToken = cookie.getValue();

        return refreshTokenUseCase.validateAndGenerateAccessToken(refreshToken)
                .flatMap(accesstoken -> {
                    AccessLoginUserResponseDto access = new AccessLoginUserResponseDto(accesstoken);
                    return Mono.just(ResponseEntity.ok().body(access));
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerHttpRequest request, ServerHttpResponse response) {
        HttpCookie cookie = request.getCookies().getFirst("refresh_token");
        if (cookie == null)
            return Mono.just(ResponseEntity.noContent().build());
        String raw = cookie.getValue();
        return refreshTokenUseCase.revoke(raw)
                .doOnSuccess(v -> {
                    ResponseCookie del = ResponseCookie
                            .from("refresh_token", "")
                            .path("/api/auth/refresh")
                            .maxAge(0)
                            .httpOnly(true)
                            .secure(true)
                            .build();
                    response.addCookie(del);

                }).thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/me")
    public Mono<ResponseEntity<Me>> me(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
            return Mono.error(new UserNotAuthenticate());
        }

        Me me = new Me(
                details.getUserId(),
                details.getUsername(),
                details.getAuthorities());

        return Mono.just(ResponseEntity.ok(me));
    }

}
