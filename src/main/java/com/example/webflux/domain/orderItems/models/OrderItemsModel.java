package com.example.webflux.domain.orderItems.models;

import java.util.UUID;

public class OrderItemsModel {
    private UUID orderItemId;
    private UUID orderId;
    private UUID listingId;
    private Integer quantity;
    private Double unitPrice;
}
