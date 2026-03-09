package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;

import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@RestControllerAdvice
public class HttpGlobalAdviceExceptions {

    // Excepcion cuando el cliente envia mas parametros de los que se les solicita
    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ApiError> handleMultipleFields(
            ServerWebInputException e,
            ServerWebExchange exchange) {
        if (e.getCause() instanceof UnrecognizedPropertyException ex) {
            return StaticError.buildError(HttpStatus.BAD_REQUEST,
                    "Not allowed property: " + ex.getPropertyName(),
                    exchange);
        }
        return StaticError.buildError(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid data!", exchange);
    }
}
