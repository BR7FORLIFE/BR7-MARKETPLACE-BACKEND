package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.auth.exceptions.AuthStatusEmailVerifiedException;
import com.example.webflux.application.emailVerificationToken.exceptions.InvalidTokenException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;

@RestControllerAdvice
public class EmailGlobalAdviceExceptions {

    // Excepcion cuando el estado de la verificacion de email no esta activa
    @ExceptionHandler(AuthStatusEmailVerifiedException.class)
    public ResponseEntity<ApiError> handleEmailNotVerified(
            AuthStatusEmailVerifiedException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), exchange);
    }

    // excepcion cuando el token de sesion no coincide con el esperado
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidEmailToken(
            InvalidTokenException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.FORBIDDEN, ex.getMessage(), exchange);
    }
}
