package com.example.webflux.infrastructure.listings.repository.postgres;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.listings.persistence.ListingEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcPostgresListingRepository extends ReactiveCrudRepository<ListingEntity, UUID> {
    Mono<Boolean> existsById(UUID id);

    @Query("""
            SELECT *
            FROM listings
            WHERE
                (:statusPublication IS NULL OR status_publication = :statusPublication)
                AND (:review IS NULL OR review = :review)
                AND (
                    (:createAtCursor IS NULL OR :id IS NULL)
                    OR (
                        create_at > :createAtCursor
                        OR (create_at = :createAtCursor AND id > :id)
                    )
                )
                ORDER BY create_at ASC, id ASC
                LIMIT :limit
            """)
    Flux<ListingEntity> getAllListings(
            @Param("createAtCursor") Instant createAtCursor,
            @Param("limit") int limit,
            @Param("id") UUID id,
            @Param("review") String review,
            @Param("statusPublication") String statusPublication);

}
