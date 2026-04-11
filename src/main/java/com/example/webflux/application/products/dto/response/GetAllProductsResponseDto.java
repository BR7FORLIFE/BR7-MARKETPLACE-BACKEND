package com.example.webflux.application.products.dto.response;

import java.util.List;

import com.example.webflux.domain.products.models.ProductModelDomain;

public record GetAllProductsResponseDto(List<ProductModelDomain> products, Boolean hasMore, int limit, int offset) {

}
