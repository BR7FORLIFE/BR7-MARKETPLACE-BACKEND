package com.example.webflux.application.listings.dto.request;

import com.example.webflux.application.listings.draft.ProductDraft;
import com.example.webflux.domain.listings.models.CurrencyEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateListingRequestDto(@Positive @NotNull Double price, @NotNull CurrencyEnum currency,
                ProductDraft product) {

}
