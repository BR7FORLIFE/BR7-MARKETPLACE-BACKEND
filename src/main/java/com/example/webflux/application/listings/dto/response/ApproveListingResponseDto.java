package com.example.webflux.application.listings.dto.response;

import java.util.UUID;

public record ApproveListingResponseDto(UUID listingId, String reviewStatus, String publicationStatus) {

}
