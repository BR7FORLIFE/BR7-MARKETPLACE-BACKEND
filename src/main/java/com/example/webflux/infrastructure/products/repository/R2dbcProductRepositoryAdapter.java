package com.example.webflux.infrastructure.products.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.domain.products.ports.ProductDomainRepositoryPort;
import com.example.webflux.infrastructure.products.mapper.ProductMapper;
import com.example.webflux.infrastructure.products.persistence.ProductEntity;
import com.example.webflux.infrastructure.products.repository.postgres.R2dbcPostgresProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class R2dbcProductRepositoryAdapter implements ProductDomainRepositoryPort {

    private final R2dbcPostgresProductRepository productRepository;

    public R2dbcProductRepositoryAdapter(R2dbcPostgresProductRepository r2dbcPostgresProductRepository) {
        this.productRepository = r2dbcPostgresProductRepository;
    }

    @Override
    public Mono<List<ProductModelDomain>> findByIds(List<UUID> productsIds) {
        return productRepository.findAllById(productsIds)
                .map(ProductMapper::toDomain)
                .collectList();
    }

    @Override
    public Flux<ProductModelDomain> findAll(Instant updateAt, int limit, int offset) {
        if (updateAt == null) {
            return productRepository.findAllWithoutDate(limit, offset)
                    .map(ProductMapper::toDomain);
        }
        return productRepository.findAllWithDate(updateAt, limit, offset)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Flux<ProductModelDomain> findAllProductsByUserId(UUID userId, int limit, int offset, String order) {
        if (order.equals("ASC")) {
            return productRepository.findAllProductsByUserIdAsc(userId, limit, offset)
                    .map(ProductMapper::toDomain);
        }
        return productRepository.findAllProductsByUserIdDesc(userId, limit, offset)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<ProductModelDomain> findByProductIdAndUserId(UUID productId, UUID userId) {
        return productRepository.findByIdAndUserId(productId, userId)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<ProductModelDomain> findByProductId(UUID productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<ProductModelDomain> findBySku(String sku) {
        return productRepository.findBySku(sku)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<ProductModelDomain> save(ProductModelDomain product) {
        return productRepository.existsById(product.getProductId())
                .flatMap(exists -> {
                    ProductEntity entity = ProductMapper.toEntity(product);

                    if (exists) {
                        entity.markNotNew();
                    }

                    return productRepository.save(entity)
                            .map(ProductMapper::toDomain);
                });
    }

    @Override
    public Mono<Boolean> existById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public Mono<Boolean> existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }
}
