package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.listings.exceptions.InvalidCursorException;
import com.example.webflux.application.listings.exceptions.ListingNotFoundException;
import com.example.webflux.domain.listings.exceptions.InvalidChangeStateException;
import com.example.webflux.domain.listings.exceptions.InvalidStateException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;

@RestControllerAdvice
public class ListingGlobalAdviceException {

    @ExceptionHandler(ListingNotFoundException.class)
    public ResponseEntity<ApiError> handleListingNotFound(
            ListingNotFoundException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }

    // Error de transicion de estado del listing
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiError> handleInvalidTransitionState(
            InvalidStateException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), exchange);
    }

    @ExceptionHandler(InvalidCursorException.class)
    public ResponseEntity<ApiError> handleInvalidCursor(
            InvalidCursorException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.CONFLICT, ex.getMessage(), exchange);
    }

    @ExceptionHandler(InvalidChangeStateException.class)
    public ResponseEntity<ApiError> handleChangeStatus(
            InvalidChangeStateException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), exchange);
    }
}
