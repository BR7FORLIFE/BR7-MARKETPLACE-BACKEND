package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.products.exceptions.RegisterProductException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;

@RestControllerAdvice
public class ProductGlobalAdviceExceptions {

    // Excepcion cuando no se encuentra el producto en la base de datos
    @ExceptionHandler(RegisterProductException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            RegisterProductException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }
}
