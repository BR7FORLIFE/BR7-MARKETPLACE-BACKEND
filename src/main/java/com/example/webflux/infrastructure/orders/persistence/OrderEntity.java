package com.example.webflux.infrastructure.orders.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Table(name = "orders")
@Data
public class OrderEntity {
    @Id
    @Column("order_id")
    private UUID orderId;

    @Column("user_id")
    private UUID userId;

    @Column("order_date")
    private Instant orderDate;

    @Column("status")
    private String status;

    @Column("total_amount")
    private Double totalAmount;
}
