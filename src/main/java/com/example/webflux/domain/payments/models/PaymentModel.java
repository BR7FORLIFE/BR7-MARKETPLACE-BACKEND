package com.example.webflux.domain.payments.models;

import java.time.Instant;
import java.util.UUID;

public final class PaymentModel {
    private final UUID paymentId;
    private final UUID orderId;
    private final Double amount;
    private final String currency;
    private final String methodPay;
    private final String statusPay;
    private final Instant paidAt;

    private PaymentModel(UUID paymentId, UUID orderId, Double amount, String currency, String methodPay,
            String statusPay, Instant paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.methodPay = methodPay;
        this.statusPay = statusPay;
        this.paidAt = paidAt;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getMethodPay() {
        return methodPay;
    }

    public String getStatusPay() {
        return statusPay;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

}
