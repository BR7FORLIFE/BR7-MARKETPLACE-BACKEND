package com.example.webflux.domain.orders.services;

import com.example.webflux.domain.orders.models.OrderStatusEnum;

public class OrderService {

    private OrderStatusEnum status;

    public void approve() {
        ensureStatus(OrderStatusEnum.PROGRESS);
        status = OrderStatusEnum.APPROVED;
    }

    public void issue() {
        ensureStatus(OrderStatusEnum.APPROVED);
        status = OrderStatusEnum.ISSUED;
    }

    public void confirm() {
        ensureStatus(OrderStatusEnum.ISSUED);
        status = OrderStatusEnum.CONFIRMED;
    }

    public void receive(boolean partial) {
        ensureStatus(OrderStatusEnum.CONFIRMED);
        status = partial ? OrderStatusEnum.PARTIALLY : OrderStatusEnum.RECEIVED;
    }

    public void invoice() {
        if (status != OrderStatusEnum.RECEIVED && status != OrderStatusEnum.PARTIALLY) {
            throw new IllegalStateException("Cannot invoice yet");
        }
        status = OrderStatusEnum.INVOICED;
    }

    public void close() {
        ensureStatus(OrderStatusEnum.INVOICED);
        status = OrderStatusEnum.CLOSED;
    }

    public void cancel() {
        if (status == OrderStatusEnum.CLOSED) {
            throw new IllegalStateException("Cannot cancel closed order");
        }
        status = OrderStatusEnum.CANCELLED;
    }

    private void ensureStatus(OrderStatusEnum expected) {
        if (status != expected) {
            throw new IllegalStateException(
                    "Invalid transition from " + status + " expected " + expected);
        }
    }

}
