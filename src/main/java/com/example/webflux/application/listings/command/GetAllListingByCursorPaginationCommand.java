package com.example.webflux.application.listings.command;

import java.util.UUID;

import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;

public record GetAllListingByCursorPaginationCommand(String cursor, int limit, ListingStatusReview review,
                ListingPublicationStatus publicationStatus, UUID userId) {

}
