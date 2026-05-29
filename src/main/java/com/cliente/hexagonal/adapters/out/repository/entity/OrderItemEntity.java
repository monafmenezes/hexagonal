package com.cliente.hexagonal.adapters.out.repository.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemEntity {
    private String productName;
    private int quantity;
    private BigDecimal price;
}