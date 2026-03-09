package com.example.webflux.infrastructure.products.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.domain.products.ports.ProductDomainRepositoryPort;
import com.example.webflux.infrastructure.products.mapper.ProductMapper;
import com.example.webflux.infrastructure.products.persistence.ProductEntity;
import com.example.webflux.infrastructure.products.repository.postgres.R2dbcPostgresProductRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcProductRepositoryAdapter implements ProductDomainRepositoryPort {

    private final R2dbcPostgresProductRepository productRepository;

    public R2dbcProductRepositoryAdapter(R2dbcPostgresProductRepository r2dbcPostgresProductRepository) {
        this.productRepository = r2dbcPostgresProductRepository;
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
