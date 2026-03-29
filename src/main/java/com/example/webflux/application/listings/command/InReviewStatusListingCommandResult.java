package com.example.webflux.application.listings.command;

import java.util.UUID;

public record InReviewStatusListingCommandResult(UUID listingId, String reviewStatus, String publicationStatus) {

}
