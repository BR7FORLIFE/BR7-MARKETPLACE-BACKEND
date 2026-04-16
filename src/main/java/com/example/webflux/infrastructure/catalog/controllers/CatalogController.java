package com.example.webflux.infrastructure.catalog.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.dto.request.CreateCatalogRequestDto;
import com.example.webflux.application.catalog.dto.response.CreateCatalogResponseDto;
import com.example.webflux.application.catalog.usecase.CatalogUseCase;
import com.example.webflux.infrastructure.security.CustomUserDetails;

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
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = details.getUserId();

        CreateCatalogCommand cmd = new CreateCatalogCommand(dto.slug(), dto.nameCatalog(), dto.typeCatalog(),
                dto.status(), dto.visible(), userId);

        return catalogUseCase.createCatalog(cmd)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(new CreateCatalogResponseDto(res.code(),
                        res.typeCatalog(), res.status(), res.visible(), res.message())));
    }

}
