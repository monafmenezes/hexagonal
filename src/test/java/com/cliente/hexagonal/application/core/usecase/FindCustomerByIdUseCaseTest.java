package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.core.exception.CustomerNotFoundException;
import com.cliente.hexagonal.application.ports.out.FindCustomerByIdOutputPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FindCustomerByIdUseCaseTest {

    @Test
    void shouldReturnCustomerWhenFound() {
        FindCustomerByIdOutputPort findCustomerByIdOutputPort = mock(FindCustomerByIdOutputPort.class);
        FindCustomerByIdUseCase useCase = new FindCustomerByIdUseCase(findCustomerByIdOutputPort);
        Customer customer = new Customer();

        when(findCustomerByIdOutputPort.find("id-1")).thenReturn(Optional.of(customer));

        Customer result = useCase.find("id-1");

        verify(findCustomerByIdOutputPort).find("id-1");
        assertSame(customer, result);
    }

    @Test
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        FindCustomerByIdOutputPort findCustomerByIdOutputPort = mock(FindCustomerByIdOutputPort.class);
        FindCustomerByIdUseCase useCase = new FindCustomerByIdUseCase(findCustomerByIdOutputPort);

        when(findCustomerByIdOutputPort.find("id-1")).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> useCase.find("id-1"));

        verify(findCustomerByIdOutputPort).find("id-1");
        assertEquals("Cliente não encontrado com o id: id-1", exception.getMessage());
    }
}
