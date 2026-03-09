package com.example.webflux.application.listings.command;

import java.util.UUID;

import com.example.webflux.application.listings.draft.ProductDraft;
import com.example.webflux.domain.listings.models.CurrencyEnum;

public record CreateListingCommand(UUID userId,ProductDraft product, Double price, CurrencyEnum currency) {

}
