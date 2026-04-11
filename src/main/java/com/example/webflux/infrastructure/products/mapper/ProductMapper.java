package com.example.webflux.infrastructure.products.mapper;

import com.example.webflux.domain.products.models.ProductModelDomain;
import com.example.webflux.infrastructure.products.persistence.ProductEntity;

public class ProductMapper {

    public static ProductModelDomain toDomain(ProductEntity productEntity) {
        return ProductModelDomain.createNew(
                productEntity.getId(),
                productEntity.getUserId(),
                productEntity.getSku(),
                productEntity.getName(),
                productEntity.getShortDescription(),
                productEntity.getLongDescription(),
                productEntity.getModel(),
                productEntity.getCreateAt(),
                productEntity.getUpdateAt());
    }

    public static ProductEntity toEntity(ProductModelDomain productModelDomain) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productModelDomain.getProductId());
        productEntity.setUserId(productModelDomain.getUserId());
        productEntity.setSku(productModelDomain.getSku());
        productEntity.setName(productModelDomain.getName());
        productEntity.setShortDescription(productModelDomain.getShortDescription());
        productEntity.setLongDescription(productModelDomain.getLongDescription());
        productEntity.setModel(productModelDomain.getModel());
        productEntity.setCreateAt(productEntity.getCreateAt());
        productEntity.setUpdateAt(productEntity.getUpdateAt());

        return productEntity;
    }
}
