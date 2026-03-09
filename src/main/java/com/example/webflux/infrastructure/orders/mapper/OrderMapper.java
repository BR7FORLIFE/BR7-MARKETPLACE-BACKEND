package com.example.webflux.infrastructure.orders.mapper;

import com.example.webflux.domain.orders.models.OrderModelDomain;
import com.example.webflux.infrastructure.orders.persistence.OrderEntity;

public class OrderMapper {

    public static OrderModelDomain toDomain(OrderEntity orderEntity) {
        return new OrderModelDomain(orderEntity.getUserId(), orderEntity.getUserId(), orderEntity.getOrderDate(),
                orderEntity.getStatus(), orderEntity.getTotalAmount());
    }

    public static OrderEntity toEntity(OrderModelDomain orderModelDomain) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(null);
        orderEntity.setUserId(orderModelDomain.getUserId());
        orderEntity.setOrderDate(orderModelDomain.getOrderDate());
        orderEntity.setStatus(orderModelDomain.getStatus());
        orderEntity.setTotalAmount(orderModelDomain.getTotalAmount());

        return orderEntity;
    }

}
