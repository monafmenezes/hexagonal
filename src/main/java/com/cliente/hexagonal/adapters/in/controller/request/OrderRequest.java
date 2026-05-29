package com.cliente.hexagonal.adapters.in.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<@Valid OrderItemRequest> items;
}