package com.example.webflux.application.orders.orchestator;

import org.springframework.stereotype.Service;

import com.example.webflux.application.orders.commands.CreateOrderCommand;
import com.example.webflux.application.orders.commands.CreateOrderCommandResult;
import com.example.webflux.application.orders.exceptions.CreateOrderException;
import com.example.webflux.application.orders.usecases.OrderUseCases;
import com.example.webflux.domain.auth.ports.UserDomainRepositoryPort;
import com.example.webflux.domain.orders.models.OrderModelDomain;
import com.example.webflux.domain.orders.ports.OrderDomainRepositoryPort;

import reactor.core.publisher.Mono;

@Service
public class OrderUseCaseImp implements OrderUseCases {

    private final OrderDomainRepositoryPort orderRepositoryPort;
    private final UserDomainRepositoryPort userDomainRepositoryPort;

    public OrderUseCaseImp(OrderDomainRepositoryPort port, UserDomainRepositoryPort userDomainRepositoryPort) {
        this.orderRepositoryPort = port;
        this.userDomainRepositoryPort = userDomainRepositoryPort;
    }

    /**
     * Caso de uso para crear una orden de compra
     */
    @Override
    public Mono<CreateOrderCommandResult> createOrder(CreateOrderCommand cmd) {
        /**
         * Primero verificamos si existe dicha orden de compra
         */
        return orderRepositoryPort.findByOrderId(cmd.orderId())
                .flatMap(existing -> Mono.<CreateOrderCommandResult>error(
                        new CreateOrderException()))
                .switchIfEmpty(
                        userDomainRepositoryPort.findByUserId(cmd.userId())
                                .switchIfEmpty(Mono.error(new CreateOrderException()))
                                .flatMap(user -> {
                                    OrderModelDomain orderModel = new OrderModelDomain(
                                            cmd.orderId(),
                                            user.getId(),
                                            cmd.orderDate(),
                                            cmd.status(),
                                            cmd.totalAmount());

                                    return orderRepositoryPort.save(orderModel);
                                })
                                .map(saved -> new CreateOrderCommandResult()));
    }
}
