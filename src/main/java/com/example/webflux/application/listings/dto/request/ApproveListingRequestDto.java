package com.example.webflux.application.listings.dto.request;

import java.util.UUID;

public record ApproveListingRequestDto(@org.hibernate.validator.constraints.UUID UUID listingId) {

}
