package com.example.webflux.domain.products.ports;

import java.util.UUID;

import com.example.webflux.domain.products.models.ProductModelDomain;

import reactor.core.publisher.Mono;

public interface ProductDomainRepositoryPort {
    Mono<ProductModelDomain> findByProductId(UUID productId);

    Mono<ProductModelDomain> findBySku(String sku);

    Mono<ProductModelDomain> save(ProductModelDomain product);

    Mono<Boolean> existById(UUID id);

    Mono<Boolean> existsBySku(String sku);
}
