package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.DeleteCustomerOutputPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteCustomerUseCaseTest {

    @Test
    void shouldFindCustomerAndDelete() {
        FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
        DeleteCustomerOutputPort deleteCustomerOutputPort = mock(DeleteCustomerOutputPort.class);
        DeleteCustomerUseCase useCase = new DeleteCustomerUseCase(findCustomerByIdInputPort, deleteCustomerOutputPort);

        when(findCustomerByIdInputPort.find("id-1")).thenReturn(new Customer());

        useCase.delete("id-1");

        verify(findCustomerByIdInputPort).find("id-1");
        verify(deleteCustomerOutputPort).delete("id-1");
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
        DeleteCustomerOutputPort deleteCustomerOutputPort = mock(DeleteCustomerOutputPort.class);
        DeleteCustomerUseCase useCase = new DeleteCustomerUseCase(findCustomerByIdInputPort, deleteCustomerOutputPort);

        when(findCustomerByIdInputPort.find("id-1")).thenThrow(new RuntimeException("Customer not found"));

        assertThrows(RuntimeException.class, () -> useCase.delete("id-1"));

        verify(deleteCustomerOutputPort, never()).delete("id-1");
    }
}