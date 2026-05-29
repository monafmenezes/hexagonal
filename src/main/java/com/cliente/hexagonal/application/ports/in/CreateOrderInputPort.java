package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Order;

public interface CreateOrderInputPort {
    Order create(Order order);
}