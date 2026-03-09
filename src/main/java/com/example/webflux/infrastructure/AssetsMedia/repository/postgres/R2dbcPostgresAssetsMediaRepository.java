package com.example.webflux.infrastructure.AssetsMedia.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.AssetsMedia.persistence.AssetsMediaEntity;

public interface R2dbcPostgresAssetsMediaRepository extends ReactiveCrudRepository<AssetsMediaEntity, UUID> {

}
