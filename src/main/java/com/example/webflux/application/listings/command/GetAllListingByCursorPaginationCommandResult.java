package com.example.webflux.application.listings.command;

import java.util.List;

import com.example.webflux.domain.listings.models.ListingModelDomain;

public record GetAllListingByCursorPaginationCommandResult(List<ListingModelDomain> listings, String cursor,
        Boolean hasMore) {

}
