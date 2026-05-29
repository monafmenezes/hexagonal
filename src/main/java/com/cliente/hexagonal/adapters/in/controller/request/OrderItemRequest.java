package com.cliente.hexagonal.adapters.in.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String productName;

    @Min(value = 1, message = "A quantidade mínima é 1")
    private int quantity;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal price;
}