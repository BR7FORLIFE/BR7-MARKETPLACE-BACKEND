package com.example.webflux.infrastructure.listings.mapper;

import com.example.webflux.domain.listings.models.CurrencyEnum;
import com.example.webflux.domain.listings.models.ListingModelDomain;
import com.example.webflux.domain.listings.models.ListingPublicationStatus;
import com.example.webflux.domain.listings.models.ListingStatusReview;
import com.example.webflux.infrastructure.listings.persistence.ListingEntity;

public class ListingMapper {

    public static ListingModelDomain toDomain(ListingEntity listingEntity) {
        return ListingModelDomain.createNew(
                listingEntity.getId(),
                listingEntity.getProductId(),
                listingEntity.getPrice(),
                CurrencyEnum.valueOf(listingEntity.getCurrency()),
                listingEntity.getIsActive(),
                ListingStatusReview.valueOf(listingEntity.getStatusReview()),
                ListingPublicationStatus.valueOf(listingEntity.getStatusPublication()),
                listingEntity.getCreateAt(),
                listingEntity.getUpdateAt());
    }

    public static ListingEntity toEntity(ListingModelDomain listingModelDomain) {
        ListingEntity listingEntity = new ListingEntity();
        listingEntity.setId(listingModelDomain.getListingId());
        listingEntity.setProductId(listingModelDomain.getProductId());
        listingEntity.setPrice(listingModelDomain.getPrice());
        listingEntity.setCurrency(listingModelDomain.getCurrency().name());
        listingEntity.setIsActive(listingModelDomain.getIsActive());
        listingEntity.setStatusReview(listingModelDomain.getReviewStatus().toString());
        listingEntity.setStatusPublication(listingModelDomain.getPublicationStatus().toString());

        return listingEntity;
    }
}
