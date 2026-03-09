package com.example.webflux.domain.listings.exceptions;

import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.domain.zGlobalDomainExceptions.DomainException;

public class InvalidStateException extends DomainException {

    public InvalidStateException(ListingStatusReview state, String action) {
        super("Cannot " + action + " Listing when status is " + state);
    }
}
