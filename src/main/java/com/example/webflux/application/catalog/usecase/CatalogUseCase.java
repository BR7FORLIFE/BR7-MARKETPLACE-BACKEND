package com.example.webflux.application.catalog.usecase;

import java.util.List;
import java.util.UUID;

import com.example.webflux.application.catalog.command.AssingmentProductsToCatalogCommandResult;
import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.command.CreateCatalogCommandResult;

import reactor.core.publisher.Mono;

public interface CatalogUseCase {
    Mono<CreateCatalogCommandResult> createCatalog(CreateCatalogCommand cmd);

    Mono<AssingmentProductsToCatalogCommandResult> assignmentProductsToCatalog(UUID userId, UUID catalogId,
            List<UUID> productsIds, String customLabel);
}
