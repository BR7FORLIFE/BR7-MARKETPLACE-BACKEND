package com.example.webflux.application.products.commands;

import java.util.List;

import com.example.webflux.domain.products.models.ProductModelDomain;

public record GetAllProductsCommandResult(List<ProductModelDomain> products, Boolean hasMore, int limit, int offset,
        String orden) {

}
