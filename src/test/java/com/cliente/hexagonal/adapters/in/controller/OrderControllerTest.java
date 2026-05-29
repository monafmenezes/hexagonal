package com.cliente.hexagonal.adapters.in.controller;

import com.cliente.hexagonal.adapters.in.controller.mapper.OrderMapper;
import com.cliente.hexagonal.adapters.in.controller.request.OrderItemRequest;
import com.cliente.hexagonal.adapters.in.controller.request.OrderRequest;
import com.cliente.hexagonal.adapters.in.controller.response.OrderItemResponse;
import com.cliente.hexagonal.adapters.in.controller.response.OrderResponse;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.domain.OrderItem;
import com.cliente.hexagonal.application.core.domain.OrderStatus;
import com.cliente.hexagonal.application.core.exception.CustomerNotFoundException;
import com.cliente.hexagonal.application.ports.in.CreateOrderInputPort;
import com.cliente.hexagonal.application.ports.in.FindOrdersByCustomerIdInputPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateOrderInputPort createOrderInputPort;

    @MockitoBean
    private FindOrdersByCustomerIdInputPort findOrdersByCustomerIdInputPort;

    @MockitoBean
    private OrderMapper orderMapper;

    @Test
    void shouldCreateOrder() throws Exception {
        OrderRequest request = buildRequest();
        OrderItem item = new OrderItem("Produto A", 2, new BigDecimal("10.00"));
        OrderResponse response = buildResponse();

        when(orderMapper.toOrderItem(any())).thenReturn(item);
        when(createOrderInputPort.create(any())).thenReturn(new Order("customer-1", List.of(item)));
        when(orderMapper.toOrderResponse(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers/customer-1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.total").value(20.00));
    }

    @Test
    void shouldReturn400WhenItemsIsEmpty() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setItems(List.of());

        mockMvc.perform(post("/api/v1/customers/customer-1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("items")));
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        OrderItem item = new OrderItem("Produto A", 1, new BigDecimal("5.00"));
        when(orderMapper.toOrderItem(any())).thenReturn(item);
        when(createOrderInputPort.create(any())).thenThrow(new CustomerNotFoundException("customer-x"));

        mockMvc.perform(post("/api/v1/customers/customer-x/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListOrdersByCustomer() throws Exception {
        OrderResponse response = buildResponse();
        when(findOrdersByCustomerIdInputPort.findByCustomerId("customer-1")).thenReturn(List.of(new Order()));
        when(orderMapper.toOrderResponse(any())).thenReturn(response);

        mockMvc.perform(get("/api/v1/customers/customer-1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    private OrderRequest buildRequest() {
        OrderItemRequest item = new OrderItemRequest();
        item.setProductName("Produto A");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("10.00"));
        OrderRequest request = new OrderRequest();
        request.setItems(List.of(item));
        return request;
    }

    private OrderResponse buildResponse() {
        OrderItemResponse itemResponse = new OrderItemResponse();
        itemResponse.setProductName("Produto A");
        itemResponse.setQuantity(2);
        itemResponse.setPrice(new BigDecimal("10.00"));
        OrderResponse response = new OrderResponse();
        response.setStatus(OrderStatus.PENDING);
        response.setTotal(new BigDecimal("20.00"));
        response.setCustomerId("customer-1");
        response.setItems(List.of(itemResponse));
        return response;
    }
}