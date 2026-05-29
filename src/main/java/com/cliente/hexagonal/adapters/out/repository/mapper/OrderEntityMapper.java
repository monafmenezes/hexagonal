package com.cliente.hexagonal.adapters.out.repository.mapper;

import com.cliente.hexagonal.adapters.out.repository.entity.OrderEntity;
import com.cliente.hexagonal.adapters.out.repository.entity.OrderItemEntity;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.domain.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderEntityMapper {
    OrderEntity toOrderEntity(Order order);
    Order toOrder(OrderEntity orderEntity);
    OrderItemEntity toOrderItemEntity(OrderItem orderItem);
    OrderItem toOrderItem(OrderItemEntity orderItemEntity);
}