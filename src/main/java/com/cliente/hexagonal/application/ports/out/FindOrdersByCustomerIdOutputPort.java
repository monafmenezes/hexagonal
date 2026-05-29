package com.cliente.hexagonal.application.ports.out;

import com.cliente.hexagonal.application.core.domain.Order;

import java.util.List;

public interface FindOrdersByCustomerIdOutputPort {
    List<Order> findByCustomerId(String customerId);
}