package com.example.webflux.application.listings.command;

import java.util.UUID;

public record ApproveListingCommandResult(UUID listingId, String reviewStatus, String publicationStatus) {

}
