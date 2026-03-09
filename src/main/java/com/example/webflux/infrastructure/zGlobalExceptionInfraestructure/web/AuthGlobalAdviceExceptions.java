package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.auth.exceptions.IncorrectPasswordException;
import com.example.webflux.application.auth.exceptions.UserAlreadyRegisterException;
import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.refreshToken.exceptions.ValidateAndRotateException;
import com.example.webflux.domain.refreshToken.exceptions.RefreshTokenExpiredException;
import com.example.webflux.domain.refreshToken.exceptions.RefreshTokenRevokedException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;
import com.nimbusds.jose.JOSEException;

@RestControllerAdvice
public class AuthGlobalAdviceExceptions {

    // === EXCEPTIONES DE AUTENTICACION ===

    // Exepciones cuando el usuario esta registrado en la plataforma
    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ApiError> handleRegisterAuth(
            UserAlreadyRegisterException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }

    // Excepciones cuando no se encuentra un usuario
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(
            UserNotFoundException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    // Excepciones cuando la contraseña es incorrecta
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiError> handleIncorrectPassword(
            IncorrectPasswordException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }

    // === REFRESH TOKEN EXCEPTIONS ===
    @ExceptionHandler(ValidateAndRotateException.class)
    public ResponseEntity<ApiError> handleValidateAndRotateRefreshToken(
            ValidateAndRotateException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }

    @ExceptionHandler(RefreshTokenRevokedException.class)
    public ResponseEntity<ApiError> handleRefreshTokenRevoked(
            RefreshTokenRevokedException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiError> handleRefreshToken(
            RefreshTokenExpiredException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }

    // === JWT EXCEPTIONS ===
    @ExceptionHandler(JOSEException.class)
    public ResponseEntity<ApiError> handleJoseExceptionJwt(
            JOSEException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }
}
