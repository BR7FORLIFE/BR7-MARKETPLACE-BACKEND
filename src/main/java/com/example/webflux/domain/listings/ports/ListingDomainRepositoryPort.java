package com.example.webflux.domain.listings.ports;

import java.time.Instant;
import java.util.UUID;

import com.example.webflux.domain.listings.models.ListingModelDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ListingDomainRepositoryPort {
    Mono<ListingModelDomain> save(ListingModelDomain listing);

    Mono<ListingModelDomain> findByListingId(UUID listingId);

    Flux<ListingModelDomain> getAllListings(Instant createAt, UUID id, int limit, String review,
            String publicationStatus);
}
