package com.cliente.hexagonal.application.core.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private String id;
    private String customerId;
    private List<OrderItem> items;
    private OrderStatus status;
    private BigDecimal total;

    public Order() {}

    public Order(String customerId, List<OrderItem> items) {
        this.customerId = customerId;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}