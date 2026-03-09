package com.example.webflux.application.products.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterProductRequestDto(
                @NotNull(message = "El nombre del producto no puede ser nulo!") @Size(min = 4) String name,
                @NotNull(message = "El sku del producto no puede ser nulo!") @Size(min = 4) String sku,
                @NotNull(message = "Debe existit una descripcion para este producto!") @Size(min = 8, max = 20) String shortDescription,
                @Size(max = 100) String longDescription,
                @NotNull(message = "Debe definir el modelo del producto!") @Size(min = 1) String model) {

}
