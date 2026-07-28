package com.example.webflux.application.catalog.dto.request;

import java.util.List;
import java.util.UUID;

public record AssingmentProductsToCatalogRequestDto(List<UUID> productsIds, String customLabel) {

}
