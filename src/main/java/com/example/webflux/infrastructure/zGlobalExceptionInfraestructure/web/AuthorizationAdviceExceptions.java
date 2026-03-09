package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.Authorization.exceptions.AssigmentRolUserException;
import com.example.webflux.application.Authorization.exceptions.RolNotFoundException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;

@RestControllerAdvice
public class AuthorizationAdviceExceptions {

    // Excepcion cuando el asignamiento de un rol ya lo posee el usuario!
    @ExceptionHandler(AssigmentRolUserException.class)
    public ResponseEntity<ApiError> handleAssigmentRol(
            AssigmentRolUserException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.CONFLICT, ex.getMessage(), exchange);
    }

    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<ApiError> handleRolNotFound(
            RolNotFoundException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }
}
