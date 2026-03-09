package com.example.webflux.infrastructure.orders.repository.postgres;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.webflux.infrastructure.orders.persistence.OrderEntity;

public interface R2dbcPostgresOrderRepository extends ReactiveCrudRepository<OrderEntity, UUID> {

}
