package com.example.webflux.application.listings.dto.response;

import java.util.UUID;

public record SuspendListingResponseDto(UUID listingId, String reviewStatus, String message) {

}
