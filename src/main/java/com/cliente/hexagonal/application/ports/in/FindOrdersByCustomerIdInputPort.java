package com.cliente.hexagonal.application.ports.in;

import com.cliente.hexagonal.application.core.domain.Order;

import java.util.List;

public interface FindOrdersByCustomerIdInputPort {
    List<Order> findByCustomerId(String customerId);
}