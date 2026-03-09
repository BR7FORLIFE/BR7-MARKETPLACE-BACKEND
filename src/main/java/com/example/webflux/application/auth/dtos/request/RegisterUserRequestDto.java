package com.example.webflux.application.auth.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequestDto(@NotNull(message = "El username no puede ser nulo! ") String username,
                @NotNull(message = "La contraseña no puede ser nulo!") String password,
                @NotNull(message = "El email no puede ser nulo!") @Email(message = "El email debe tener un formato correcto!") String email) {

}
