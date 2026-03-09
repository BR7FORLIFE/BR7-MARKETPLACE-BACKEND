package com.example.webflux.infrastructure.zGlobalExceptionInfraestructure.helpers;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime localDateTime,
        int status,
        String error,
        String message,
        String path) {

}
