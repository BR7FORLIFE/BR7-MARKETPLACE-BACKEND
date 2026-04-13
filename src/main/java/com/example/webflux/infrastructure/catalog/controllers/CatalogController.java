package com.example.webflux.infrastructure.catalog.controllers;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.catalog.dto.request.CreateCatalogRequestDto;
import com.example.webflux.application.catalog.dto.response.CreateCatalogResponseDto;
import com.example.webflux.application.catalog.usecase.CatalogUseCase;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    public final CatalogUseCase catalogUseCase;

    public CatalogController(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<CreateCatalogResponseDto>> createCatalog(@RequestBody CreateCatalogRequestDto dto,
            Authentication authentication) {
        return null;
    }

}
