package com.example.webflux.application.listings.command;

import java.util.UUID;

public record CreateListingCommandResult(UUID listingId, String status ,String message) {

}
