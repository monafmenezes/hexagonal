package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.domain.OrderItem;
import com.cliente.hexagonal.application.core.exception.CustomerNotFoundException;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.CreateOrderOutputPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOrderUseCaseTest {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
    private final CreateOrderOutputPort createOrderOutputPort = mock(CreateOrderOutputPort.class);
    private final CreateOrderUseCase useCase = new CreateOrderUseCase(findCustomerByIdInputPort, createOrderOutputPort);

    @Test
    void shouldCreateOrderWhenCustomerExists() {
        Order order = new Order("customer-1", List.of(new OrderItem("Produto A", 2, new BigDecimal("10.00"))));
        when(findCustomerByIdInputPort.find("customer-1")).thenReturn(new Customer());
        when(createOrderOutputPort.create(order)).thenReturn(order);

        Order result = useCase.create(order);

        verify(findCustomerByIdInputPort).find("customer-1");
        verify(createOrderOutputPort).create(order);
        assertSame(order, result);
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        Order order = new Order("customer-x", List.of(new OrderItem("Produto A", 1, new BigDecimal("5.00"))));
        when(findCustomerByIdInputPort.find("customer-x")).thenThrow(new CustomerNotFoundException("customer-x"));

        assertThrows(CustomerNotFoundException.class, () -> useCase.create(order));
        verify(createOrderOutputPort, never()).create(any());
    }
}