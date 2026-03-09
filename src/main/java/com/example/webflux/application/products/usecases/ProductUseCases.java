package com.example.webflux.application.products.usecases;

import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.commands.RegisterProductCommandResult;

import reactor.core.publisher.Mono;

public interface ProductUseCases {
    Mono<RegisterProductCommandResult> registerProduct(RegisterProductCommand cmd);
}
