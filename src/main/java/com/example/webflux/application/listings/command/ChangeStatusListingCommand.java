package com.example.webflux.application.listings.command;

import java.util.UUID;

import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;

public record ChangeStatusListingCommand(UUID listingId, ListingStatusReview review,
        ListingPublicationStatus publicationStatus) {

}
