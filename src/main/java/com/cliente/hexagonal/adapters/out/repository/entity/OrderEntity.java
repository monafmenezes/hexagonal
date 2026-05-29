package com.cliente.hexagonal.adapters.out.repository.entity;

import com.cliente.hexagonal.application.core.domain.OrderStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "orders")
public class OrderEntity {

    @Id
    private String id;
    private String customerId;
    private List<OrderItemEntity> items;
    private OrderStatus status;
    private BigDecimal total;
}