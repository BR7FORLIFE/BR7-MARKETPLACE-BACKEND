package com.example.webflux.application.listings.dto.response;

import java.util.UUID;

public record RejectedListingResponseDto(UUID listingId, String reviewStatus, String message) {

}
