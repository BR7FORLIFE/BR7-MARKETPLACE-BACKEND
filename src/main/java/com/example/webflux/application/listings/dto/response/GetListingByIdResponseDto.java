package com.example.webflux.application.listings.dto.response;

import com.example.webflux.domain.listings.models.ListingModelDomain;

public record GetListingByIdResponseDto(ListingModelDomain listing, String error) {

}
