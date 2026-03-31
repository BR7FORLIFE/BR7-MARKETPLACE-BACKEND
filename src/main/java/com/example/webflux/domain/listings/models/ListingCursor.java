package com.example.webflux.domain.listings.models;

import java.time.Instant;
import java.util.UUID;

public final class ListingCursor {
    private final int version;
    private final Instant createAt;
    private final UUID id;

    public ListingCursor(int version, Instant createAt, UUID id) {
        this.version = version;
        this.createAt = createAt;
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public UUID getId() {
        return id;
    }

}
