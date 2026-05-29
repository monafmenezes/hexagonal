package com.cliente.hexagonal.adapters.in.controller.mapper;

import com.cliente.hexagonal.adapters.in.controller.request.OrderItemRequest;
import com.cliente.hexagonal.adapters.in.controller.response.OrderResponse;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.domain.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderItem toOrderItem(OrderItemRequest orderItemRequest);
    OrderResponse toOrderResponse(Order order);
}