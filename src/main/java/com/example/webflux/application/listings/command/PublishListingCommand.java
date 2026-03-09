package com.example.webflux.application.listings.command;

import java.util.UUID;

public record PublishListingCommand(UUID listingId) {

}
