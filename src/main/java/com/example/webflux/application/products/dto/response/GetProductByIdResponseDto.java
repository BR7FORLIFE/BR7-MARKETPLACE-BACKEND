package com.example.webflux.application.products.dto.response;

import com.example.webflux.domain.products.models.ProductModelDomain;

public record GetProductByIdResponseDto(ProductModelDomain product) {
    
}
