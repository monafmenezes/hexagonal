package com.cliente.hexagonal.application.core.usecase;

import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import com.cliente.hexagonal.application.ports.out.FindAddresByZipCodeOutputPort;
import com.cliente.hexagonal.application.ports.out.InsertCustomerOutputPort;
import com.cliente.hexagonal.application.ports.out.SendCpfForValidationOutputPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InsertCustomerUseCaseTest {

    @Test
    void shouldFindAddressAndPersistCustomer() {
        FindAddresByZipCodeOutputPort findAddressByZipCodeOutputPort = mock(FindAddresByZipCodeOutputPort.class);
        InsertCustomerOutputPort insertCustomerOutputPort = mock(InsertCustomerOutputPort.class);
        SendCpfForValidationOutputPort sendCpfForValidationOutputPort = mock(SendCpfForValidationOutputPort.class);
        InsertCustomerUseCase useCase = new InsertCustomerUseCase(
                findAddressByZipCodeOutputPort,
                insertCustomerOutputPort,
                sendCpfForValidationOutputPort);
        Customer customer = new Customer();
        customer.setCpf("12345678901");
        Address address = new Address("Rua A", "Fortaleza", "CE");

        when(findAddressByZipCodeOutputPort.find("60100-000")).thenReturn(address);

        useCase.insert(customer, "60100-000");

        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(findAddressByZipCodeOutputPort).find("60100-000");
        verify(insertCustomerOutputPort).insert(customerCaptor.capture());
        verify(sendCpfForValidationOutputPort).send("12345678901");
        assertSame(customer, customerCaptor.getValue());
        assertSame(address, customer.getAddress());
    }
}
