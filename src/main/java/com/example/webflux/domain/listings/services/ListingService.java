package com.example.webflux.domain.listings.services;

import java.util.List;

import com.example.webflux.domain.listings.exceptions.InvalidChangeStateException;
import com.example.webflux.domain.listings.models.ListingModelDomain;
import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;

public class ListingService {

    public static ListingModelDomain verifyChangeStatus(ListingModelDomain listingModelDomain,
            ListingStatusReview review,
            ListingPublicationStatus publicationStatus) {

        if (review == null && publicationStatus == null) {
            throw new InvalidChangeStateException();
        }

        ListingModelDomain update = listingModelDomain;

        if (review != null) {
            // en el switch hay que tomar en cuenta que los casos que se van a evaluar
            // es lo que el administrador quiere hacer con el listing
            switch (review) {
                case IN_REVIEW:
                    update = listingModelDomain.submitForReview();
                    break;

                case PUBLISHED:
                    update = listingModelDomain.approveReview();
                    break;

                case NEEDS_FIX:
                    update = listingModelDomain.requestFix();
                    break;

                case REJECTED:
                    update = listingModelDomain.rejectReview();
                    break;

                default:
                    break;
            }
        }

        if (publicationStatus != null) {
            switch (publicationStatus) {
                case ACTIVE:
                    update = listingModelDomain.publish();
                    break;

                case SUSPENDED:
                    update = listingModelDomain.suspend();
                    break;
                default:
                    break;
            }
        }

        return update;
    }
}
