package com.example.webflux.application.listings.command;

import java.util.UUID;

public record GetListingByIdCommand(UUID userId, UUID listingId) {

}
