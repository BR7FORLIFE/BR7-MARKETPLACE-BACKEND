package com.example.webflux.application.orders.usecases;

import com.example.webflux.application.orders.commands.CreateOrderCommand;
import com.example.webflux.application.orders.commands.CreateOrderCommandResult;

import reactor.core.publisher.Mono;

public interface OrderUseCases {
    Mono<CreateOrderCommandResult> createOrder(CreateOrderCommand createOrderCommand);
}
