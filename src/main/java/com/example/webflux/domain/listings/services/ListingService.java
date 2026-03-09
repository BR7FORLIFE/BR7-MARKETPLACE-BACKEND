package com.example.webflux.domain.listings.services;

import com.example.webflux.domain.listings.models.ListingStatusReview;

public class ListingService {

    private ListingStatusReview status;

    public ListingService(ListingStatusReview status) {
        this.status = status;
    }

    public void submit() {
        this.status = status.submit();
    }

    public void approve() {
        this.status.approve();
    }

    public void requestFix() {
        this.status.requestFix();
    }

    public void reject() {
        this.status.reject();
    }

    public void resubmit() {
        this.status.resubmit();
    }

    public ListingStatusReview getStatus() {
        return this.status;
    }

}
