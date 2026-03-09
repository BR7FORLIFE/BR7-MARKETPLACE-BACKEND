package com.example.webflux.domain.listings.models;

import java.time.Instant;
import java.util.UUID;

import com.example.webflux.domain.listings.exceptions.InvalidStateException;

public final class ListingModelDomain {
    private final UUID listingId;
    private final UUID productId;
    private final Double price;
    private final CurrencyEnum currency;

    private final Boolean isActive;

    private final ListingStatusReview reviewStatus;
    private final ListingPublicationStatus publicationStatus;

    private final Instant createdAt;
    private final Instant updatedAt;

    private ListingModelDomain(
            UUID listingId,
            UUID productId,
            Double price,
            CurrencyEnum currency,
            Boolean isActive,
            ListingStatusReview reviewStatus,
            ListingPublicationStatus publicationStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.listingId = listingId;
        this.productId = productId;
        this.price = price;
        this.currency = currency;
        this.isActive = isActive;
        this.reviewStatus = reviewStatus;
        this.publicationStatus = publicationStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Factory for persistence / rehydration
    public static ListingModelDomain createNew(
            UUID listingId,
            UUID productId,
            Double price,
            CurrencyEnum currency,
            Boolean isActive,
            ListingStatusReview reviewStatus,
            ListingPublicationStatus publicationStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new ListingModelDomain(
                listingId,
                productId,
                price,
                currency,
                false,
                reviewStatus,
                publicationStatus,
                createdAt,
                updatedAt);
    }

    // Factory for user creation
    public static ListingModelDomain createDraft(
            UUID productId,
            Double price,
            CurrencyEnum currency) {
        Instant now = Instant.now();
        return new ListingModelDomain(
                UUID.randomUUID(),
                productId,
                price,
                currency,
                false,
                ListingStatusReview.DRAFT,
                ListingPublicationStatus.INACTIVE,
                now,
                now);
    }

    // Admin moderations

    public ListingModelDomain submitForReview() {
        return changeReviewStatus(reviewStatus.submit());
    }

    public ListingModelDomain approveReview() {
        return changeReviewStatus(reviewStatus.approve());
    }

    public ListingModelDomain requestFix() {
        return changeReviewStatus(reviewStatus.requestFix());
    }

    public ListingModelDomain rejectReview() {
        return changeReviewStatus(reviewStatus.reject());
    }

    private ListingModelDomain changeReviewStatus(ListingStatusReview newStatus) {
        return new ListingModelDomain(
                listingId,
                productId,
                price,
                currency,
                isActive,
                newStatus,
                publicationStatus,
                createdAt,
                Instant.now());
    }

    // publication workflow (VISIBLE)
    public ListingModelDomain publish() {
        if (reviewStatus != ListingStatusReview.PUBLISHED) {
            throw new InvalidStateException(reviewStatus, "publish");
        }
        return changePublicationStatus(ListingPublicationStatus.ACTIVE);
    }

    public ListingModelDomain suspend() {
        return changePublicationStatus(ListingPublicationStatus.SUSPENDED);
    }

    private ListingModelDomain changePublicationStatus(ListingPublicationStatus newStatus) {
        return new ListingModelDomain(
                listingId,
                productId,
                price,
                currency,
                isActive,
                reviewStatus,
                newStatus,
                createdAt,
                Instant.now());
    }

    // getters
    public UUID getListingId() {
        return listingId;
    }

    public ListingStatusReview getReviewStatus() {
        return reviewStatus;
    }

    public ListingPublicationStatus getPublicationStatus() {
        return publicationStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getProductId() {
        return productId;
    }

    public Double getPrice() {
        return price;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public Boolean getIsActive() {
        return isActive;
    }
}
