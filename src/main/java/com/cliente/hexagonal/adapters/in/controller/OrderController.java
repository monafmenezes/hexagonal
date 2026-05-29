package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.OrderMapper;
import com.cliente.hexagonal.adapters.in.controller.request.OrderRequest;
import com.cliente.hexagonal.adapters.in.controller.response.OrderResponse;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.domain.OrderItem;
import com.cliente.hexagonal.application.ports.in.CreateOrderInputPort;
import com.cliente.hexagonal.application.ports.in.FindOrdersByCustomerIdInputPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/orders")
public class OrderController {

    @Autowired
    private CreateOrderInputPort createOrderInputPort;

    @Autowired
    private FindOrdersByCustomerIdInputPort findOrdersByCustomerIdInputPort;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@PathVariable String customerId,
                                                @Valid @RequestBody OrderRequest orderRequest) {
        List<OrderItem> items = orderRequest.getItems().stream()
                .map(orderMapper::toOrderItem)
                .toList();
        Order order = new Order(customerId, items);
        return ResponseEntity.ok(orderMapper.toOrderResponse(createOrderInputPort.create(order)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findByCustomerId(@PathVariable String customerId) {
        List<OrderResponse> orders = findOrdersByCustomerIdInputPort.findByCustomerId(customerId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
        return ResponseEntity.ok(orders);
    }
}