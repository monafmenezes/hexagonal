package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Order;

public interface CreateOrderOutputPort {
    Order create(Order order);
}