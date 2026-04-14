package com.example.webflux.application.catalog.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.command.CreateCatalogCommandResult;
import com.example.webflux.application.catalog.usecase.CatalogUseCase;

import reactor.core.publisher.Mono;

@Service
public class CatalogUseCaseImp implements CatalogUseCase {

    @Override
    public Mono<CreateCatalogCommandResult> createCatalog(CreateCatalogCommand cmd) {
        return null;
    }
}
