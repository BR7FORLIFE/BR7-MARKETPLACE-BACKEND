package com.example.webflux.domain.products.ports;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.example.webflux.domain.products.models.ProductModelDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductDomainRepositoryPort {

    Flux<ProductModelDomain> findAll(Instant updateAt, int limit, int offset);

    Flux<ProductModelDomain> findAllProductsByUserId(UUID userId, int limit, int offset, String order);

    Mono<ProductModelDomain> findByProductIdAndUserId(UUID productId, UUID userId);

    Mono<ProductModelDomain> findByProductId(UUID productId);

    Mono<ProductModelDomain> findBySku(String sku);

    Mono<ProductModelDomain> save(ProductModelDomain product);

    Mono<Boolean> existById(UUID id);

    Mono<Boolean> existsBySku(String sku);

    Mono<List<ProductModelDomain>> findByIds(List<UUID> productsIds);
}
