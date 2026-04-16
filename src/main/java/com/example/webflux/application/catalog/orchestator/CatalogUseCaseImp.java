package com.example.webflux.application.catalog.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.command.CreateCatalogCommandResult;
import com.example.webflux.application.catalog.exception.CatalogNotFoundException;
import com.example.webflux.application.catalog.ports.CatalogPort;
import com.example.webflux.application.catalog.usecase.CatalogUseCase;
import com.example.webflux.domain.catalog.service.CatalogService;
import com.example.webflux.domain.catalogs.models.CatalogModelDomain;

import reactor.core.publisher.Mono;

@Service
public class CatalogUseCaseImp implements CatalogUseCase {

    private final CatalogPort catalogPort;

    public CatalogUseCaseImp(CatalogPort catalogPort) {
        this.catalogPort = catalogPort;
    }

    @Override
    public Mono<CreateCatalogCommandResult> createCatalog(CreateCatalogCommand cmd) {
        return catalogPort.existBySlug(cmd.slug())
                .flatMap(exists -> {

                    if (exists) {
                        return Mono.error(new CatalogNotFoundException());
                    }

                    String catalogCode = CatalogService.generateCodeCatalog();

                    CatalogModelDomain newCatalog = CatalogModelDomain.createDraft(cmd.userId(), catalogCode,
                            cmd.slug(), cmd.nameCatalog(), cmd.typeCatalog(), cmd.status(), cmd.visible());

                    return catalogPort.save(newCatalog)
                            .map(saved -> new CreateCatalogCommandResult(saved.getCode(), saved.getType(),
                                    saved.getStatus(),
                                    saved.getVisible(), "Catalog created succesfull!"));
                });
    }
}
