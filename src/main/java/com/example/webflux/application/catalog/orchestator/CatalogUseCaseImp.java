package com.example.webflux.application.catalog.orchestator;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webflux.application.auth.exceptions.UserNotFoundException;
import com.example.webflux.application.auth.ports.UserDomainRepositoryPort;
import com.example.webflux.application.catalog.command.AssingmentProductsToCatalogCommandResult;
import com.example.webflux.application.catalog.command.CreateCatalogCommand;
import com.example.webflux.application.catalog.command.CreateCatalogCommandResult;
import com.example.webflux.application.catalog.exception.CatalogNotFoundException;
import com.example.webflux.application.catalog.ports.CatalogPort;
import com.example.webflux.application.catalog.usecase.CatalogUseCase;
import com.example.webflux.domain.catalogs.models.CatalogModelDomain;
import com.example.webflux.domain.catalogs.models.CatalogProductItemDomain;
import com.example.webflux.domain.catalogs.service.CatalogService;
import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.domain.products.ports.ProductDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class CatalogUseCaseImp implements CatalogUseCase {

    private final CatalogPort catalogPort;
    private final UserDomainRepositoryPort userPort;
    private final ProductDomainRepositoryPort productPort;

    public CatalogUseCaseImp(CatalogPort catalogPort, UserDomainRepositoryPort userDomainRepositoryPort,
            ProductDomainRepositoryPort productDomainRepositoryPort) {
        this.catalogPort = catalogPort;
        this.userPort = userDomainRepositoryPort;
        this.productPort = productDomainRepositoryPort;
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

    @Transactional
    @Override
    public Mono<AssingmentProductsToCatalogCommandResult> assignmentProductsToCatalog(UUID userId, UUID catalogId,
            List<UUID> productsIds, String customLabel) {
        return userPort.findByUserId(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .then(catalogPort.findByCatalogId(catalogId))
                .switchIfEmpty(Mono.error(new CatalogNotFoundException()))
                .flatMap(catalog -> Mono.zip(
                        Mono.just(catalog),
                        productPort.findByIds(productsIds)))
                .flatMap(tuple -> {

                    CatalogModelDomain catalog = tuple.getT1();
                    List<ProductModelDomain> products = tuple.getT2();

                    List<CatalogProductItemDomain> catalogProductItemDomains = products
                            .stream()
                            .map(product -> CatalogProductItemDomain.createDraft(catalog.getCatalogId(),
                                    product.getProductId(),
                                    customLabel))
                            .toList();

                    return catalogPort.saveAll(catalogProductItemDomains)
                            .thenReturn(new AssingmentProductsToCatalogCommandResult(catalogId,
                                    "Products assignment succesfull!"));
                });
    }
}
