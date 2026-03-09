package com.example.webflux.application.listings.command;

import java.util.UUID;

public record RejectedListingCommand(UUID listingId) {

}
