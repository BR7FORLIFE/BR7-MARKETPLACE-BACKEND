package com.example.webflux.infrastructure.orders.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.orders.dto.response.CreateOrderResponseDto;
import com.example.webflux.application.orders.usecases.OrderUseCases;

import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class OrderController {

    private final OrderUseCases orderUseCase;

    public OrderController(OrderUseCases orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping("/users/{userId}/orders")
    public Mono<ResponseEntity<CreateOrderResponseDto>> createOrder() {
        return null;
    }
}
