package com.example.webflux.application.listings.command;

import com.example.webflux.domain.listings.models.ListingModelDomain;

public record GetListingByIdCommandResult(ListingModelDomain listing, String error) {

}
