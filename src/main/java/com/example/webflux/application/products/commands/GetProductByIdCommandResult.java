package com.example.webflux.application.products.commands;

import com.example.webflux.domain.products.models.ProductModelDomain;

public record GetProductByIdCommandResult(ProductModelDomain product) {

}
