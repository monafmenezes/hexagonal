package com.cliente.hexagonal.adapters.in.controller.mapper;

import com.cliente.hexagonal.adapters.in.controller.request.CustomerRequest;
import com.cliente.hexagonal.adapters.in.controller.response.CustomerResponse;
import com.cliente.hexagonal.application.core.domain.Address;
import com.cliente.hexagonal.application.core.domain.Customer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerMapperTest {

    private final CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);

    @Test
    void shouldMapRequestToDomainAndIgnoreGeneratedFields() {
        CustomerRequest request = new CustomerRequest();
        request.setName("Maria");
        request.setCpf("12345678901");
        request.setZipCode("60100-000");

        Customer customer = customerMapper.toCustomer(request);

        assertEquals("Maria", customer.getName());
        assertEquals("12345678901", customer.getCpf());
        assertNull(customer.getId());
        assertNull(customer.getAddress());
        assertFalse(customer.getValidCpf());
    }

    @Test
    void shouldMapDomainToResponse() {
        Customer customer = new Customer(
                "id-1",
                "Maria",
                "12345678901",
                new Address("Rua A", "Fortaleza", "CE"),
                true);

        CustomerResponse response = customerMapper.toCustomerResponse(customer);

        assertEquals("Maria", response.getName());
        assertEquals("12345678901", response.getCpf());
        assertEquals(Boolean.TRUE, response.getIsValidCpf());
        assertEquals("Rua A", response.getAddress().getStreet());
        assertEquals("Fortaleza", response.getAddress().getCity());
        assertEquals("CE", response.getAddress().getState());
    }
}
