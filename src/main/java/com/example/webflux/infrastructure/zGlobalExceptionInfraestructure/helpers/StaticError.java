package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

public class StaticError {
    public static ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            ServerWebExchange exchange) {

        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                exchange.getRequest().getPath().value());

        return ResponseEntity.status(status).body(error);
    }
}
