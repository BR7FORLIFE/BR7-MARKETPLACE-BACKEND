package com.example.webflux.infrastructure.orders.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.example.webflux.domain.orders.models.OrderModelDomain;
import com.example.webflux.domain.orders.ports.OrderDomainRepositoryPort;
import com.example.webflux.infrastructure.orders.mapper.OrderMapper;
import com.example.webflux.infrastructure.orders.repository.postgres.R2dbcPostgresOrderRepository;

import reactor.core.publisher.Mono;

@Repository
public class R2dbcOrderRepositoryAdapter implements OrderDomainRepositoryPort {

    private final R2dbcPostgresOrderRepository orderRepository;

    public R2dbcOrderRepositoryAdapter(R2dbcPostgresOrderRepository r2dbcPostgresOrderRepository) {
        this.orderRepository = r2dbcPostgresOrderRepository;
    }

    @Override
    public Mono<OrderModelDomain> findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper::toDomain);
    }

    @Override
    public Mono<OrderModelDomain> save(OrderModelDomain orderModelDomain) {
        return orderRepository.save(OrderMapper.toEntity(orderModelDomain))
                .map(OrderMapper::toDomain);
    }
}
