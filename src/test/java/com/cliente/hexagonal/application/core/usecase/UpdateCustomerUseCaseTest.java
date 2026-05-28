package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.cliente.hexagonal.application.ports.out.FindAddresByZipCodeOutputPort;
import com.cliente.hexagonal.application.ports.out.UpdateCustomerOutputPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateCustomerUseCaseTest {

    @Test
    void shouldFindAddressAndPersistUpdatedCustomer() {
        FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
        FindAddresByZipCodeOutputPort findAddresByZipCodeOutputPort = mock(FindAddresByZipCodeOutputPort.class);
        UpdateCustomerOutputPort updateCustomerOutputPort = mock(UpdateCustomerOutputPort.class);
        UpdateCustomerUseCase useCase = new UpdateCustomerUseCase(
                findCustomerByIdInputPort,
                findAddresByZipCodeOutputPort,
                updateCustomerOutputPort);
        Customer customer = new Customer("id-1", "Maria", "12345678901", null, true);
        Address address = new Address("Rua A", "Fortaleza", "CE");

        when(findCustomerByIdInputPort.find("id-1")).thenReturn(customer);
        when(findAddresByZipCodeOutputPort.find("60100-000")).thenReturn(address);

        useCase.update(customer, "60100-000");

        verify(findCustomerByIdInputPort).find("id-1");
        verify(findAddresByZipCodeOutputPort).find("60100-000");
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(updateCustomerOutputPort).update(customerCaptor.capture());
        assertSame(customer, customerCaptor.getValue());
        assertSame(address, customer.getAddress());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        FindCustomerByIdInputPort findCustomerByIdInputPort = mock(FindCustomerByIdInputPort.class);
        FindAddresByZipCodeOutputPort findAddresByZipCodeOutputPort = mock(FindAddresByZipCodeOutputPort.class);
        UpdateCustomerOutputPort updateCustomerOutputPort = mock(UpdateCustomerOutputPort.class);
        UpdateCustomerUseCase useCase = new UpdateCustomerUseCase(
                findCustomerByIdInputPort,
                findAddresByZipCodeOutputPort,
                updateCustomerOutputPort);
        Customer customer = new Customer("id-1", "Maria", "12345678901", null, true);

        when(findCustomerByIdInputPort.find("id-1")).thenThrow(new RuntimeException("Customer not found"));

        assertThrows(RuntimeException.class, () -> useCase.update(customer, "60100-000"));

        verify(findAddresByZipCodeOutputPort, never()).find("60100-000");
        verify(updateCustomerOutputPort, never()).update(customer);
    }
}