package com.example.webflux.application.listings.dto.response;

import java.util.List;

import com.example.webflux.domain.listings.models.ListingModelDomain;

public record GetAllListingResponseDto(List<ListingModelDomain> listings, String cursor, Boolean hasMore) {

}
