package com.example.webflux.application.catalog.usecase;

import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.command.CreateCatalogCommandResult;

import reactor.core.publisher.Mono;

public interface CatalogUseCase {
    Mono<CreateCatalogCommandResult> createCatalog(CreateCatalogCommand cmd);
}
