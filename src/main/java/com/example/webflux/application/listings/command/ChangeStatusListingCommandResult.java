package com.example.webflux.application.listings.command;

import java.time.Instant;

public record ChangeStatusListingCommandResult(String message, Instant updateAt) {

}
