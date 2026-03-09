package com.example.webflux.infrastructure.listings.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.listings.models.ListingModelDomain;
import com.example.webflux.domain.listings.ports.ListingDomainRepositoryPort;
import com.example.webflux.infrastructure.listings.mapper.ListingMapper;
import com.example.webflux.infrastructure.listings.persistence.ListingEntity;
import com.example.webflux.infrastructure.listings.repository.postgres.R2dbcPostgresListingRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcListingRepositoryAdapter implements ListingDomainRepositoryPort {

    private final R2dbcPostgresListingRepository listingRepository;

    public R2dbcListingRepositoryAdapter(R2dbcPostgresListingRepository r2dbcPostgresListingRepository) {
        this.listingRepository = r2dbcPostgresListingRepository;
    }

    @Override
    public Mono<ListingModelDomain> save(ListingModelDomain listing) {
        return listingRepository.existsById(listing.getListingId())
                .flatMap(exists -> {
                    ListingEntity entity = ListingMapper.toEntity(listing);

                    if (exists) {
                        entity.markNotNew();
                    }

                    return listingRepository.save(entity)
                            .map(ListingMapper::toDomain);
                });
    }

    @Override
    public Mono<ListingModelDomain> findByListingId(UUID listingId) {
        return listingRepository.findById(listingId)
                .map(ListingMapper::toDomain);
    }

}
