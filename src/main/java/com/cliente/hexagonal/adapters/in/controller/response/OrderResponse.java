package com.cliente.hexagonal.adapters.in.controller.response;

import com.cliente.hexagonal.application.core.domain.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {
    private String id;
    private String customerId;
    private List<OrderItemResponse> items;
    private OrderStatus status;
    private BigDecimal total;
}