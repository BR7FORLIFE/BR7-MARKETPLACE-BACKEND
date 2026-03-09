package com.example.webflux.application.auth.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginUserRequestDto(
                @NotNull(message = "el email no puede ser nulo!") @Email(message = "Debe tener un formato correcto de email") String email,
                @NotNull(message = "El password no puede ser nulo!") String password) {

}
