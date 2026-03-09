package com.example.webflux.application.products.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.products.commands.RegisterProductCommand;
import com.example.webflux.application.products.commands.RegisterProductCommandResult;
import com.example.webflux.application.products.exceptions.RegisterProductException;
import com.example.webflux.application.products.usecases.ProductUseCases;
import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.domain.products.ports.ProductDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class ProductUseCaseImp implements ProductUseCases {

    private final ProductDomainRepositoryPort port;

    public ProductUseCaseImp(ProductDomainRepositoryPort port) {
        this.port = port;
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
}
