package com.example.webflux.application.listings.dto.request;

import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;

public record ChangeStatusRequestDto(ListingStatusReview review, ListingPublicationStatus publicationStatus) {

}
