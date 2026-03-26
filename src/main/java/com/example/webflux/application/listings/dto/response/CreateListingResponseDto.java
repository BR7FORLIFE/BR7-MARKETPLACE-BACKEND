package com.example.webflux.application.listings.dto.response;

import java.util.UUID;

public record CreateListingResponseDto(UUID listingId, String status, String message) {

}
