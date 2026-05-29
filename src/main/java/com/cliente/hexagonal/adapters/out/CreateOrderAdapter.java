package com.cliente.hexagonal.adapters.out;

import com.cliente.hexagonal.adapters.out.repository.OrderRepository;
import com.cliente.hexagonal.adapters.out.repository.entity.OrderEntity;
import com.cliente.hexagonal.adapters.out.repository.mapper.OrderEntityMapper;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.ports.out.CreateOrderOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderAdapter implements CreateOrderOutputPort {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEntityMapper orderEntityMapper;

    @Override
    public Order create(Order order) {
        OrderEntity entity = orderEntityMapper.toOrderEntity(order);
        OrderEntity saved = orderRepository.save(entity);
        return orderEntityMapper.toOrder(saved);
    }
}