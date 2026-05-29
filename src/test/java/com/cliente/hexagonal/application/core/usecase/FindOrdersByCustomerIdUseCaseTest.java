package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.core.domain.Order;
import com.cliente.hexagonal.application.core.exception.CustomerNotFoundException;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.FindOrdersByCustomerIdOutputPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindOrdersByCustomerIdUseCaseTest {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
    private final FindOrdersByCustomerIdOutputPort outputPort = mock(FindOrdersByCustomerIdOutputPort.class);
    private final FindOrdersByCustomerIdUseCase useCase =
            new FindOrdersByCustomerIdUseCase(findCustomerByIdInputPort, outputPort);

    @Test
    void shouldReturnOrdersWhenCustomerExists() {
        when(findCustomerByIdInputPort.find("customer-1")).thenReturn(new Customer());
        when(outputPort.findByCustomerId("customer-1")).thenReturn(List.of(new Order()));

        List<Order> result = useCase.findByCustomerId("customer-1");

        verify(findCustomerByIdInputPort).find("customer-1");
        verify(outputPort).findByCustomerId("customer-1");
        assertEquals(1, result.size());
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        when(findCustomerByIdInputPort.find("customer-x")).thenThrow(new CustomerNotFoundException("customer-x"));

        assertThrows(CustomerNotFoundException.class, () -> useCase.findByCustomerId("customer-x"));
        verify(outputPort, never()).findByCustomerId(any());
    }
}