package com.example.webflux.domain.listings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.domain.listings.services.ListingService;

@DisplayName("Listing status test")
public class listingDomainService {

    // domain test for the all functionability
    @Test
    void submittingDraftMovesToInReview() {
        // GIVEN
        ListingStatusReview status = ListingStatusReview.DRAFT;

        // WHEN
        ListingStatusReview newStatus = status.submit();

        // THEN
        assertEquals(ListingStatusReview.IN_REVIEW, newStatus);
    }

    @Test
    public void approvingInReviewMovesToPublished() {
        // GIVEN
        ListingStatusReview status = ListingStatusReview.IN_REVIEW;

        // WHEN
        ListingStatusReview newStatus = status.approve();

        // THEM
        assertEquals(ListingStatusReview.PUBLISHED, newStatus);
    }

    // if someone break or change this rules
    @Test
    public void approvingDraftIsNotAllowed() {
        ListingStatusReview status = ListingStatusReview.DRAFT;

        assertThrows(IllegalStateException.class, status::approve);
    }

    // test with Listing services
    @Test
    public void submitingListingChangesStatusToInReview() {

        // GIVEN
        ListingStatusReview status = ListingStatusReview.DRAFT;
        ListingService service = new ListingService(status);

        // WHEN
        service.approve();
        ;

        // THEN
        assertEquals(ListingStatusReview.IN_REVIEW, service.getStatus());
    }

}
