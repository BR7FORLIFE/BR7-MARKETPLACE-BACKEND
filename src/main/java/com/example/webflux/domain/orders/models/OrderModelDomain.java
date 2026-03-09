package com.example.webflux.domain.orders.models;

import java.time.Instant;
import java.util.UUID;

public final class OrderModelDomain {
    private final UUID orderId;
    private final UUID userId;
    private final Instant orderDate;
    private final String status;
    private final Double totalAmount;

    public OrderModelDomain(UUID orderId, UUID userId, Instant orderDate, String status, Double totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
