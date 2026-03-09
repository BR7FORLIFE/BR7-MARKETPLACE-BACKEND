package com.example.webflux.application.orders.commands;

import java.time.Instant;
import java.util.UUID;


public record CreateOrderCommand(UUID orderId, UUID userId, Instant orderDate, String status,
        Double totalAmount) {

}
