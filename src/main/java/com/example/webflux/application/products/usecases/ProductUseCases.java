package com.example.webflux.application.products.usecases;

import com.example.webflux.application.products.commands.GetAllProductsCommand;
import com.example.webflux.application.products.commands.GetAllProductsCommandResult;
import com.example.webflux.application.products.commands.GetProductByIdCommand;
import com.example.webflux.application.products.commands.GetProductByIdCommandResult;
import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.commands.RegisterProductCommandResult;

import reactor.core.publisher.Mono;

public interface ProductUseCases {
    Mono<RegisterProductCommandResult> registerProduct(RegisterProductCommand cmd);

    Mono<GetAllProductsCommandResult> getAllProducts(GetAllProductsCommand cmd);

    Mono<GetProductByIdCommandResult> getProductById(GetProductByIdCommand cmd);
}
