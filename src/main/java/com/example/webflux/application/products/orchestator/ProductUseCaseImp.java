package com.example.webflux.application.products.orchestator;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.webflux.application.Authorization.ports.RolUserRepositoryPort;
import com.example.webflux.application.products.commands.GetAllProductsCommand;
import com.example.webflux.application.products.commands.GetAllProductsCommandResult;
import com.example.webflux.application.products.commands.GetProductByIdCommand;
import com.example.webflux.application.products.commands.GetProductByIdCommandResult;
import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.commands.RegisterProductCommandResult;
import com.example.webflux.application.products.exceptions.ProductNotFoundException;
import com.example.webflux.application.products.exceptions.RegisterProductException;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.domain.Authorization.services.AuthorizationService;
import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.domain.products.ports.ProductDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class ProductUseCaseImp implements ProductUseCases {

    private final ProductDomainRepositoryPort port;
    private final RolUserRepositoryPort rolPort;

    public ProductUseCaseImp(ProductDomainRepositoryPort port, RolUserRepositoryPort rolUserRepositoryPort) {
        this.port = port;
        this.rolPort = rolUserRepositoryPort;
    }

    @Override
    public Mono<RegisterProductCommandResult> registerProduct(RegisterProductCommand cmd) {
        return port.existsBySku(cmd.sku())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.<RegisterProductCommandResult>error(new RegisterProductException());
                    }

                    ProductModelDomain product = ProductModelDomain.createDraft(cmd.userId(), cmd.sku(), cmd.name(),
                            cmd.shortDescription(), cmd.longDescription(), cmd.model());

                    return port.save(product)
                            .map(saved -> {
                                return new RegisterProductCommandResult(saved.getProductId(), saved.getName());
                            });

                });
    }

    @Override
    public Mono<GetAllProductsCommandResult> getAllProducts(GetAllProductsCommand cmd) {

        return rolPort.obtainRolByUserId(cmd.userId())
                .collectList()
                .flatMap(roles -> {

                    boolean canViewAll = AuthorizationService.canViewAll(roles);
                    String orden = "ASC".equalsIgnoreCase(cmd.orden()) ? "ASC" : "DESC";
                    int limitPlusOne = cmd.limit() + 1;

                    Mono<List<ProductModelDomain>> productsMono;

                    if (canViewAll) {
                        productsMono = port.findAll(cmd.updateAt(), limitPlusOne, cmd.offset())
                                .collectList();
                    } else {
                        productsMono = port.findAllProductsByUserId(
                                cmd.userId(),
                                limitPlusOne,
                                cmd.offset(),
                                orden).collectList();
                    }

                    return productsMono.map(products -> {

                        boolean hasMore = products.size() > cmd.limit();

                        if (hasMore) {
                            products = products.subList(0, cmd.limit());
                        }

                        return new GetAllProductsCommandResult(
                                products,
                                hasMore,
                                cmd.limit(),
                                cmd.offset(),
                                orden);
                    });
                });
    }

    @Override
    public Mono<GetProductByIdCommandResult> getProductById(GetProductByIdCommand cmd) {
        return port.findByProductIdAndUserId(cmd.productId(), cmd.userId())
                .switchIfEmpty(Mono.error(new ProductNotFoundException()))
                .map(product -> new GetProductByIdCommandResult(product));
    }
}
