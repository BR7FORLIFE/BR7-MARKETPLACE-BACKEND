package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import com.example.webflux.application.listings.exceptions.ApproveListingException;
import com.example.webflux.application.listings.exceptions.CreateListingException;
import com.example.webflux.domain.listings.exceptions.InvalidStateException;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.ApiError;
import com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers.StaticError;

@RestControllerAdvice
public class ListingGlobalAdviceException {

    // error de creacion de listing
    @ExceptionHandler(CreateListingException.class)
    public ResponseEntity<ApiError> handleCreateListing(
            CreateListingException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.CONFLICT, ex.getMessage(), exchange);
    }

    // Error de aprovacion de listing
    @ExceptionHandler(ApproveListingException.class)
    public ResponseEntity<ApiError> handleApproveListing(
            ApproveListingException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), exchange);
    }

    // Error de transicion de estado del listing
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiError> handleInvalidTransitionState(
            InvalidStateException ex,
            ServerWebExchange exchange) {
        return StaticError.buildError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), exchange);
    }
}
