package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.OrderRepository;
import com.cliente.hexagonal.adapters.out.repository.mapper.OrderEntityMapper;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.ports.out.FindOrdersByCustomerIdOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FindOrdersByCustomerIdAdapter implements FindOrdersByCustomerIdOutputPort {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    @Override
    public List<Order> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(orderEntityMapper::toOrder)
                .toList();
    }
}